
function waterFall(col) {
    // window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function() {
    //     if (window.orientation === 180 || window.orientation === 0) {
    //         alert('竖屏状态！');
    //     }
    //     if (window.orientation === 90 || window.orientation === -90 ){
    //         alert('横屏状态！');
    //     }
    // }, false);
    // 1- 确定图片的宽度 - 滚动条宽度
    var pageWidth = getClient().width-8;
    var columns = col;
    if(col == -1){
        columns = Math.ceil((getClient().width)/500)
    }

    var itemWidth = parseInt(pageWidth/columns) ; //得到item的宽度
    $(".item").width(itemWidth); //设置到item的宽度
    var left = []
    var arr = [];

    if(itemWidth<300){
        with($(".top p,.bottom p")){
            css('font-size',"15px")
        }
        $(".imgct").css("border-radius","20px")

    }else {
        with($(".top p,.bottom p")){
            css('font-size',"30px")
        }
        $(".imgct").css("border-radius","40px")
    }


    $(".masonry .item").each(function(i){
        var height = array[i].height;
        var width = array[i].width;
        var bi = itemWidth/width; //获取缩小的比值
        var boxheight = parseInt(height*bi); //图片的高度*比值 = item的高度

        if (i < columns) {
            let left1 = (itemWidth) * i
            left.push(left1)
            // 2- 确定第一行
            $(this).css({
                top:0,
                left:left1
            });
            arr.push(boxheight);

        } else {
            // 其他行
            // 3- 找到数组中最小高度  和 它的索引
            var minHeight = arr[0];
            var index = 0;

            for (var j = 0; j < arr.length; j++) {
                if (minHeight > arr[j]) {
                    minHeight = arr[j];
                    index = j;
                }
            }
            // 4- 设置下一行的第一个盒子位置
            // top值就是最小列的高度
            $(this).css({
                top:minHeight,
                left:left[index]
            });

            // 5- 修改最小列的高度
            // 最小列的高度 = 当前自己的高度 + 拼接过来的高度
            arr[index] = arr[index] + boxheight ;
        }
    });
    cols = arr.length
    document.cookie="col="+cols

}

function checkcolumn(){
    console.log("页面宽度 "+getClient().width)
    return Math.floor((getClient().width - 200)/500)

    // if(getClient().width>3500 ){
    //     cols = 6
    // }
    // if(getClient().width>2500){
    //     cols = 5
    // }else if(getClient().width>1900){
    //     cols = 4
    // }else {
    //     cols = 3
    // }
}

//clientWidth 处理兼容性
function getClient() {
    return {
        // width: window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
        width: document.documentElement.clientWidth || document.body.clientWidth,
        // height: window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
        height: document.documentElement.clientHeight || document.body.clientHeight
    }
}