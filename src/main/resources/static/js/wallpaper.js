var url
var images = $(".img");
var lfbt
var rtbt
flag = 1
page = 1
var pagesize = parseInt($(".item").length);

function callback(entries) {
    for (let i of entries) {
        if (i.isIntersecting) {
            let img = i.target;
            let trueSrc = img.getAttribute("data-src");
            img.setAttribute("src", trueSrc);
            img.onload = function (){
                let tp = img.getAttribute("id").substring(3)
                console.log('succ:'+tp)
                if(parseInt(tp)== pagesize*(page-1)+pagesize-7){
                    getpic()
                }
            }
            img.onerror = function (){
                console.log('error:' + img.getAttribute("id") + '  reloading')
                img.setAttribute("src", trueSrc+'#retry=1');
            }
            var clickTimeId = null;
            img.ondblclick = function (){
                clearTimeout(clickTimeId)
                bookmark(trueSrc.substring(5),img.getAttribute("id").substring(3))
            }
            img.onclick = function (){
                clearTimeout(clickTimeId);
                var currImg = $(this);
                var currnum = 1;
                var maxnum = parseInt(currImg.attr("alt"))

                clickTimeId = setTimeout(function (){
                    document.documentElement.style.overflowY = 'hidden'; //禁止底层div滚动

                    if(maxnum == 1) {
                        coverLayer(1);
                        var tempContainer = $('<div class=tempContainer></div>');//图片容器
                        with (tempContainer) {//width方法等同于$(this)
                            appendTo("body");
                            var windowWidth = $(window).width();
                            var windowHeight = $(window).height();
                            //获取图片原始宽度、高度
                            var orignImg = new Image();
                            orignImg.src = currImg.attr("src");
                            var currImgWidth = orignImg.width;
                            var currImgHeight = orignImg.height;
                            if (currImgWidth < windowWidth) {//为了让图片不失真，当图片宽度较小的时候，保留原图
                                if (currImgHeight < windowHeight) {
                                    var topHeight = (windowHeight - currImgHeight) / 2;
                                    if (topHeight > 35) {/*此处为了使图片高度上居中显示在整个手机屏幕中：因为在android,ios的微信中会有一个title导航，35为title导航的高度*/
                                        topHeight = topHeight - 35;
                                        css('top', topHeight);
                                    } else {
                                        css('top', 0);
                                    }
                                    console.log("1")
                                    html('<img border=0 src=' + currImg.attr('src') + '>');
                                } else {
                                    css('top', 0);
                                    console.log("2")
                                    html('<img border=0 src=' + currImg.attr('src') + ' height=' + windowHeight + '>');
                                }
                            } else {
                                var currImgChangeHeight = (currImgHeight * windowWidth) / currImgWidth;
                                if (currImgChangeHeight < windowHeight) {
                                    var topHeight = (windowHeight - currImgChangeHeight) / 2;
                                    if (topHeight > 35) {
                                        topHeight = topHeight - 35;
                                        css('top', topHeight);
                                    } else {
                                        css('top', 0);
                                    }
                                    html('<img border=0 src=' + currImg.attr('src') + ' width=' + windowWidth + ';>');
                                } else {
                                    css('top', 0);
                                    html('<img border=0 src=' + currImg.attr('src') + ' height=' + windowHeight + ';>');
                                    // html('<img border=0 src=' + currImg.attr('src') + ' width='+windowWidth+'; height='+windowHeight+'>');
                                }
                            }
                        }
                        tempContainer.click(function () {
                            $(this).remove();
                            document.documentElement.style.overflowY = 'auto'; //恢复底层div滚动
                            coverLayer(0);
                        });
                    }else {
                        var src = currImg.attr("data-src")
                        var curimg = 0
                        var imgArr = []
                        // var carousel1 = '<div id="carousel1">' +
                        //     '  <ul></ul>'+
                        //     '  <div id="leftArrow" class="iconfont icon-arrow-lift"></div> <!-- 左箭头切换按钮 -->\n' +
                        //     '  <div id="rightArrow" class="iconfont icon-arrow-right"></div> <!-- 右箭头切换按钮 -->\n' +
                        //     '  <div id="sliderBtn"></div>' +
                        //     '</div>';
                        var carousel1 = '<div id="carousel1">' +
                            '  <div class="imgct1">' +
                            '    <div class="imgbox">' +
                            '    </div>' +
                            '  </div>  <div class="view1">' +
                            '    <ul class="view2">' +
                            '    </ul>' +
                            '  </div><div class="numtag1"><span></span></div>' +
                            '  </div> ';
                        $("body").append(carousel1)
                        function Toimg(i) {
                            var orignImg = new Image();
                            let src1 = src + '?num=' + i
                            var windowWidth = $(window).width();
                            var windowHeight = $(".imgct1").height();

                            orignImg.onload = function () {
                                if (i == curimg) {
                                    with ($(".imgbox")) {
                                        var currImgWidth = orignImg.width;
                                        var currImgHeight = orignImg.height;
                                        let tmp = currImgWidth / windowWidth
                                        let tmp1 = currImgHeight / windowHeight
                                        if (currImgWidth <= windowWidth && currImgHeight <= windowHeight) {
                                            css('width', currImgWidth)
                                            css('height', currImgHeight)
                                        } else if (currImgWidth <= windowWidth && currImgHeight > windowHeight) {
                                            css('width', currImgWidth / tmp1)
                                            css('height', windowHeight)
                                        } else if (currImgWidth > windowWidth && currImgHeight < windowHeight) {
                                            css('width', windowWidth)
                                            css('height', currImgHeight / tmp)
                                        } else if (currImgWidth > windowWidth && currImgHeight > windowHeight) {
                                            if (tmp > tmp1) {
                                                css('width', currImgWidth / tmp)
                                                css('height', currImgHeight / tmp)
                                            } else {
                                                css('width', currImgWidth / tmp1)
                                                css('height', currImgHeight / tmp1)

                                            }
                                        }
                                    }
                                    $(".imgbox img")[0].setAttribute("src", src1)
                                }
                            }
                            with ($(".imgbox")){
                                css('width',50)
                                css('height',50)
                                html('<img border=0 src="/image/loading2.gif">');
                            }
                            $(".numtag1 span")[0].innerText = (i+1)+'/'+maxnum
                            orignImg.src = src1;
                        }

                        function lfbtn(){
                            if(curimg<=0){
                                return
                            }else {
                                Toimg(--curimg)
                                animate(slideContainer, ofset+200);
                                ofset+=200
                            }
                        }
                        lfbt = lfbtn
                        function rtbtn(){
                            if(curimg>=maxnum-1){
                                return
                            }else {
                                Toimg(++curimg)
                                animate(slideContainer, ofset-200);
                                ofset-=200
                            }
                        }
                        rtbt = rtbtn
                        var slideContainer = document.querySelector(".view1 > ul");
                        var ofset = slideContainer.offsetLeft
                        var key = false;
                        var firstTime = 0;
                        var lastTime = 0;
                        var startx, starty;
                        listen()
                        $(".imgct1").dblclick(function () {

                            event.stopPropagation();

                            if($(".view1").css("visibility")=='visible') {
                                $(".imgct1").css("height", "100%")
                                $(".view1").css("visibility", "hidden")
                                Toimg(curimg)
                            }else{
                                $(".imgct1").css("height", "calc(100% - 200px)")
                                $(".view1").css("visibility", "visible")
                                Toimg(curimg)
                            }
                            // $("#carousel1")[0].remove()

                        });
                        for(let i=0;i<maxnum;i++){
                            let orignImg = new Image();
                            let trueSrc = src+'?num='+i
                            orignImg.onload = function (){
                                console.log("img "+i+" width:"+orignImg.width)
                                imgArr.push(orignImg)
                            }
                            orignImg.onerror = function (){
                                console.log('error:' + orignImg.getAttribute("id") + '  reloading')
                                $('#imgg'+i)[0].setAttribute("src", trueSrc+'&retry=1');
                            }
                            orignImg.src = trueSrc;
                            $(".view1 ul").append('<li><img id="imgg'+i+'" src="'+trueSrc+'" alt="'+i+'"></li>')
                            $("#imgg"+i).click(function (){
                                // while(orignImg.width==0){
                                //
                                //   orignImg = new Image();
                                //   orignImg.src = trueSrc+"#"+new Date().getTime();
                                //   console.log(orignImg.width + '  ' +orignImg.src)
                                // }
                                let target = parseInt($(this).attr("alt"))
                                ofset = ofset+(curimg - target)*200
                                animate(slideContainer,ofset)
                                // console.log("offset2:"+$(".view2")[0].offsetLeft)
                                curimg = i
                                Toimg(i)
                            })
                        }
                        Toimg(curimg)
                    //     var imgArr = []; // 图片数组
                    //     var curIndex = 0; // 当前显示图片
                    //     // var timer = null; // 定时器
                    //     var clickAllow = true; // 锁，是否允许用户点击
                    //     var btnList = []; // 右下角切换按钮组
                    //     var startx, starty;
                    //
                    //     //获得角度
                    //     function getAngle(angx, angy) {
                    //         return Math.atan2(angy, angx) * 180 / Math.PI;
                    //     };
                    //
                    //     //根据起点终点返回方向 1向上滑动 2向下滑动 3向左滑动 4向右滑动 0点击事件
                    //     function getDirection(startx, starty, endx, endy) {
                    //         var angx = endx - startx;
                    //         var angy = endy - starty;
                    //         var result = 0;
                    //
                    //         //如果滑动距离太短
                    //         if (Math.abs(angx) < 2 && Math.abs(angy) < 2) {
                    //             return result;
                    //         }
                    //
                    //         var angle = getAngle(angx, angy);
                    //         if (angle >= -135 && angle <= -45) {
                    //             result = 1;
                    //         } else if (angle > 45 && angle < 135) {
                    //             result = 2;
                    //         } else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
                    //             result = 3;
                    //         } else if (angle >= -45 && angle <= 45) {
                    //             result = 4;
                    //         }
                    //         return result;
                    //     }
                    //     function slide(slideContainer , targetIndex = curIndex + 1){
                    //         var width = 0; // 计算切换图片要滑动的距离
                    //         if(targetIndex > curIndex){
                    //             for(let i=curIndex;i<targetIndex;++i) {
                    //                 width+=imgArr[i].width;
                    //                 console.log("width2:"+width)
                    //             } // 正向切换则计算本图片到后续图片宽度
                    //         }else{
                    //             if(targetIndex === -1) width = imgArr[imgArr.length-1].width; // 特殊处理第一张图片
                    //             else for(let i=targetIndex;i<curIndex;++i) width+=imgArr[i].width; // 逆向切换处理宽度
                    //             console.log("width1:"+width)
                    //         }
                    //         clickAllow = false; // 不允许用户点击
                    //         var step = width/60; // 动态设置步长
                    //         step = targetIndex > curIndex ? step : -step; // 正向逆向切换
                    //         var curConLeft = slideContainer.offsetLeft; // 获取ul的left
                    //         var distanceMoved = 0; // 已经移动的距离
                    //         var slideInterval = setInterval(function (){ // 此定时器是为了实现切换动画
                    //             if(Math.abs(width - distanceMoved) > Math.abs(step)){ // 边界判定，判断已移动距离以及应移动距离的差与步长关系
                    //                 curConLeft -= step; // 大于步长则不断移动
                    //                 slideContainer.style.left = `${curConLeft - step}px`; // 移动
                    //                 distanceMoved += Math.abs(step); // 已移动距离加步长
                    //             }else{
                    //                 clearInterval(slideInterval); // 若最后移动距离不足步长，则清除动画定时器
                    //                 var directMove = step > 0 ? (curConLeft - width + distanceMoved) : (curConLeft + width - distanceMoved); // 正向移动与逆向移动的计算方式不同
                    //                 slideContainer.style.left = `${directMove}px`; // 直接完成此次动画
                    //                 distanceMoved = 0; // 重设移动距离为0
                    //                 curIndex = targetIndex; // 设置当前index
                    //                 if(curIndex === imgArr.length){ // index加1，判断是否为最后一张来作边缘处理
                    //                     curIndex = 0; // 最后一张则重置index
                    //                     slideContainer.style.left = `-${imgArr[0].width}px`;  // 重置ul
                    //                 }else if (curIndex === -1) {
                    //                     curIndex = imgArr.length-1; // 第一张重置index
                    //                     slideContainer.style.left = `-${slideContainer.offsetWidth - imgArr[imgArr.length-1].width - imgArr[0].width}px`;  // 重置ul
                    //                 }
                    //                 switchBtnActive(); // 右下角按钮的切换
                    //                 clickAllow = true; // 允许点击
                    //             }
                    //         }, 10);
                    //     }
                    //
                    //     function switchBtnActive(){
                    //         btnList.forEach((v) => {
                    //             v.className = "unitBtn"; // 设置其他按钮为灰色
                    //         })
                    //         btnList[curIndex] .className = "unitBtn active"; // 设置当前按钮为蓝色
                    //     }
                    //
                    //     function createBtnGroup(carousel,slideContainer){
                    //         document.getElementById("leftArrow").addEventListener('click',(e) => {
                    //             // clearInterval(timer); // 清除定时器，避免手动切换时干扰
                    //             event.stopPropagation();
                    //             if(clickAllow) slide(slideContainer,curIndex-1); // 允许点击则切换上一张
                    //             // timer = setInterval(() => {slide(slideContainer)},config.interval); // 重设定时器
                    //         })
                    //         document.getElementById("rightArrow").addEventListener('click',(e) => {
                    //             // clearInterval(timer); // 清除定时器，避免手动切换时干扰
                    //             event.stopPropagation();
                    //             if(clickAllow) slide(slideContainer,curIndex+1); // 允许点击则切换下一张
                    //             // timer = setInterval(() => {slide(slideContainer)},config.interval); // 重设定时器
                    //         })
                    //         var sliderBtn = document.getElementById("sliderBtn"); // 获取按钮容器的引用
                    //         imgArr.forEach((v,i) => {
                    //             let btn = document.createElement("div"); // 制作按钮
                    //             btn.className = i === 0 ?  "unitBtn active" : "unitBtn"; // 初设蓝色与灰色按钮样式
                    //             btn.addEventListener('click',(e) => {
                    //                 // clearInterval(timer); // 清除定时器，避免手动切换时干扰
                    //                 event.stopPropagation();
                    //                 if(clickAllow) slide(slideContainer,i); // // 允许点击则切换
                    //                 // timer = setInterval(() => {slide(slideContainer)},config.interval); // 重设定时器
                    //             })
                    //             btnList.push(btn); // 添加按钮到按钮组
                    //             sliderBtn.appendChild(btn); // 追加按钮到按钮容器
                    //         })
                    //     }
                    //
                    //
                    //     function edgeDispose(slideContainer){
                    //         var li = document.createElement("li"); // 创建<li>
                    //         var img = document.createElement("img"); // 创建新的<img>
                    //         img.src = imgArr[0].src; // 设置图片src
                    //         li.appendChild(img); // 追加<img>到<li>
                    //         slideContainer.appendChild(li); // 将第一张图片追加到轮播图的最后，作边缘处理
                    //         var li2 = document.createElement("li"); // 创建<li>
                    //         var img2 = document.createElement("img"); // 创建新的<img>
                    //         img2.src = imgArr[imgArr.length-1].src; // 设置图片src
                    //         li2.appendChild(img2); // 追加<img>到<li>
                    //         slideContainer.insertBefore(li2,slideContainer.firstChild); // 将最后一张图片追加到轮播图的最前，作边缘处理
                    //         slideContainer.style.left = `-${imgArr[0].width}px`; // 重设ul位置
                    //         $("#carousel")[0].addEventListener("touchstart", function(e){
                    //             startx = e.touches[0].pageX;
                    //             starty = e.touches[0].pageY;
                    //         }, false);
                    //
                    //         //手指离开屏幕
                    //         $("#carousel")[0].addEventListener("touchend", function(e) {
                    //             var endx, endy;
                    //             endx = e.changedTouches[0].pageX;
                    //             endy = e.changedTouches[0].pageY;
                    //             var direction = getDirection(startx, starty, endx, endy);
                    //             switch (direction) {
                    //                 // case 0:
                    //                 //     alert("点击！");
                    //                 //     break;
                    //                 case 1:
                    //                     $(this).remove();
                    //                     console.log("remove")
                    //                     coverLayer(0);
                    //                     break;
                    //                 // case 2:
                    //                 //     alert("向下！");
                    //                 //     break;
                    //                 case 3:
                    //                     if(clickAllow) slide(slideContainer,curIndex+1); // 允许点击则切换下一张
                    //                     break;
                    //                 case 4:
                    //                     if(clickAllow) slide(slideContainer,curIndex-1); // 允许点击则切换上一张
                    //                     break;
                    //                 // default:
                    //                 //     $(this).remove();
                    //                 //     console.log("remove")
                    //                 //     coverLayer(0);
                    //                 //     break;
                    //             }
                    //         }, true);
                    //         var key = false;
                    //         var firstTime = 0;
                    //         var lastTime = 0;
                    //         $("#carousel")[0].onmousedown=function(e){
                    //             var a1=e.screenX;
                    //             firstTime = new Date().getTime();
                    //             event.stopPropagation();
                    //             $("#carousel")[0].onmouseup=function (e){
                    //                 var a2=e.screenX;
                    //                 lastTime = new Date().getTime();
                    //                 if( (lastTime - firstTime) < 120){
                    //                     key = true;
                    //                 }else {
                    //                     if (a1 > a2) {
                    //                         if (clickAllow) slide(slideContainer, curIndex + 1);
                    //                     }
                    //                     if (a1 < a2) {
                    //                         if (clickAllow) slide(slideContainer, curIndex - 1);
                    //                     }
                    //                 }
                    //             }
                    //         }
                    //         $("#carousel").click(function () {
                    //             if(key) {
                    //                 $(this).remove();
                    //                 console.log("remove")
                    //                 event.stopPropagation();
                    //                 coverLayer(0);
                    //             }
                    //         });
                    //     }
                    //     (function start() {
                    //         let src = currImg.attr("src")
                    //         coverLayer(1)
                    //         $("#carousel").height($(window).height() - 100)
                    //         // $("#carousel").width($(window).width() - 300)
                    //
                    //             var carousel = document.getElementById("carousel");
                    //             for(let i=0;i<maxnum;i++){
                    //                 var orignImg = new Image();
                    //                 orignImg.src = src+'?num='+i;
                    //                 orignImg.width = 700;
                    //                 $("#carousel ul").append('<li><img src="'+src+'?num='+i+'"></li>')
                    //                 orignImg.onload=imgArr.push(orignImg)
                    //                 orignImg.onerror = function (){
                    //                     console.log('error:' + img.getAttribute("id") + '  reloading')
                    //                     orignImg.setAttribute("src", trueSrc+'#retry=1');
                    //                 }
                    //             }
                    //             // document.querySelectorAll("#carousel img").forEach(v => imgArr.push(v)); // 获取所有图片组成数组
                    //             console.log(imgArr)
                    //             var slideContainer = document.querySelector("#carousel > ul"); // 获取ul也就是一行图片的容器
                    //             edgeDispose(slideContainer); // 对边缘处理
                    //             createBtnGroup(carousel,slideContainer);
                    //
                    //         } )();
                    //     // carousel.click(function () {
                    //     //
                    //     // });

                   }

                }, 250);
            }
            observer.unobserve(img);
        }
    }

}
const observer = new IntersectionObserver(callback);
// for (let i of images) {
//   console.log(i)
//   observer.observe(i);
// }
images.each(function(i, img) {
    observer.observe(img)
});
//获得角度
function getAngle(angx, angy) {
    return Math.atan2(angy, angx) * 180 / Math.PI;
};

//根据起点终点返回方向 1向上滑动 2向下滑动 3向左滑动 4向右滑动 0点击事件
function getDirection(startx, starty, endx, endy) {
    var angx = endx - startx;
    var angy = endy - starty;
    var result = 0;

    //如果滑动距离太短
    if (Math.abs(angx) < 2 && Math.abs(angy) < 2) {
        return result;
    }

    var angle = getAngle(angx, angy);
    if (angle >= -135 && angle <= -45) {
        result = 1;
    } else if (angle > 45 && angle < 135) {
        result = 2;
    } else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
        result = 3;
    } else if (angle >= -45 && angle <= 45) {
        result = 4;
    }
    return result;
}
function bigImg(index){
    document.documentElement.style.overflowY = 'hidden'; //禁止底层div滚动
    fullscr()
    var carousel1 = '<div id="carousel1">' +
        '  <div class="imgct1">' +
        '    <div class="imgbox">' +
        '    </div>' +
        '  </div>  <div class="view1">' +
        '    <ul class="view2">' +
        '    </ul>' +
        '  </div><div class="numtag1"><span></span></div>' +
        '  </div>';
    $("body").append(carousel1)
    var imgArr = []
    curimg = index
    var onkey = true
    document.querySelectorAll(".masonry img").forEach(v => imgArr.push(v))
    var maxnum  = imgArr.length
    function Toimg(i){
        // console.log("ost: "+slideContainer.offsetLeft)
        var orignImg = new Image();
        let src1 = imgArr[i].getAttribute("src")
        if(src1=='#'){
            src1 = imgArr[i].getAttribute("data-src")
        }
        var windowWidth = $(window).width();
        var windowHeight = $(".imgct1").height();

        orignImg.onload = function (){
            if(curimg == i) {
                with ($(".imgbox")) {
                    var currImgWidth = orignImg.width;
                    var currImgHeight = orignImg.height;
                    let tmp = currImgWidth / windowWidth
                    let tmp1 = currImgHeight / windowHeight
                    if (currImgWidth <= windowWidth && currImgHeight <= windowHeight) {

                        css('width', currImgWidth)
                        css('height', currImgHeight)
                    } else if (currImgWidth <= windowWidth && currImgHeight > windowHeight) {

                        css('width', currImgWidth / tmp1)
                        css('height', windowHeight)
                    } else if (currImgWidth > windowWidth && currImgHeight <= windowHeight) {

                        css('width', windowWidth)
                        css('height', currImgHeight / tmp)
                    } else if (currImgWidth > windowWidth && currImgHeight > windowHeight) {
                        if (tmp > tmp1) {

                            css('width', currImgWidth / tmp)
                            css('height', currImgHeight / tmp)
                        } else {
                            css('width', currImgWidth / tmp1)
                            css('height', currImgHeight / tmp1)

                        }
                    }else {
                        console.log("????")
                    }
                }
                $(".imgbox img")[0].setAttribute("src", src1)
            }
        }
        with ($(".imgbox")){
            css('width',50)
            css('height',50)
            html('<img border=0  src="/image/loading2.gif">');
        }
        $(".numtag1 span")[0].innerText = (i+1)+'/'+maxnum
        orignImg.src = src1;
    }
    function lfbtn(){
        if(curimg<=0){
            return
        }else {
            Toimg(--curimg)
            animate(slideContainer, ofset+200);
            ofset+=200
        }
    }
    lfbt = lfbtn
    function rtbtn(){
        if(curimg>=maxnum-1){
            if(curimg == maxnum-1 && onkey){
                getpic()
                onkey = false
                var timer = null
                timer = setInterval(function () {
                        if($(".item").length > maxnum){
                            clearInterval(timer)
                            removedialog()
                            bigImg(maxnum)
                        }
                    },200)
            }
        }else {
            Toimg(++curimg)
            animate(slideContainer, ofset-200);
            ofset-=200
        }
    }
    rtbt = rtbtn
    var slideContainer = document.querySelector(".view1 > ul");
    var ofset = slideContainer.offsetLeft
    var key = false;
    var firstTime = 0;
    var lastTime = 0;
    var startx, starty;
    listen()
    $(".imgct1").dblclick(function () {

        event.stopPropagation();

        if($(".view1").css("visibility")=='visible') {
            $(".imgct1").css("height", "100%")
            $(".view1").css("visibility", "hidden")

            Toimg(curimg)
        }else{
            $(".imgct1").css("height", "calc(100% - 200px)")
            $(".view1").css("visibility", "visible")
            Toimg(curimg)
        }
        // $("#carousel1")[0].remove()

    });
    for(let i=0;i<maxnum;i++){
        let orignImg = new Image();
        let src = imgArr[i].getAttribute("src")
        if(src == '#'){
            src = imgArr[i].getAttribute("data-src")
        }
        orignImg.onload = function (){
            console.log("img "+i+" onload")
        }
        orignImg.onerror = function (){
            console.log('error:' + orignImg.getAttribute("id") + '  reloading')
            $('#imgg'+i)[0].setAttribute("src", src+'#retry='+new Date().getTime());
        }
        orignImg.src = src;
        $(".view1 ul").append('<li><img id="imgg'+i+'" src="'+src+'" alt="'+i+'"></li>')
        $("#imgg"+i).click(function (){
            // while(orignImg.width==0){
            //
            //   orignImg = new Image();
            //   orignImg.src = trueSrc+"#"+new Date().getTime();
            //   console.log(orignImg.width + '  ' +orignImg.src)
            // }
            let target = parseInt($(this).attr("alt"))
            ofset = ofset+(curimg - target)*200
            animate(slideContainer,ofset)
            // console.log("offset2:"+$(".view2")[0].offsetLeft)
            curimg = i
            Toimg(i)
        })
    }
    ofset = ofset + -(index*200)
    // animate(slideContainer,ofset)
    $(".view2").css("left",ofset)
    Toimg(curimg)
}

function tiao1(){
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: {"id":id2, "flag":"1"},
        success:function (result){
            console.log("res:"+result)
            if(result == 1){
                $("#follow")[0].innerText="已关注"
                $("#follow")[0].removeAttribute("onclick")

                console.log("已关注")
            }else {
                console.log("出现错误")
            }
        }
    })
}
function coverLayer(tag){
    with($('.over')){
        if(tag==1){
            css('height',$(document).height());
            css('display','block');
            css('opacity',1);
            css("background-color","#FFFFFF");
            css("background-color","rgba(0,0,0,0.7)" );  //蒙层透明度
            // click(coverLayer(0))
        }
        else{
            css('display','none');
        }
    }
}
function fullscr(){

    var docElm = document.documentElement;
    if('ontouchstart' in document.documentElement){

    }
//W3C

    else if (docElm.requestFullscreen) {


        docElm.requestFullscreen();

    }

//FireFox

    else if (docElm.mozRequestFullScreen) {

        docElm.mozRequestFullScreen();

    }

//Chrome等

    else if (docElm.webkitRequestFullScreen) {

        docElm.webkitRequestFullScreen();

    }

//IE11

    else if (elem.msRequestFullscreen) {

        elem.msRequestFullscreen();

    }
}
function removedialog(){
    document.documentElement.style.overflowY = 'auto'
    $("#carousel1")[0].remove()

    waterFall(-1);
}
function checkloaded() {

    var total = $(".item").length
    console.log(total)
    if (total==15*(page+1)){
        var id3 = $('#'+'img' + total)[0]

        id3.onload = function (){
            console.log("加载完了")
            waterFall()
            flag = 1
            $(".loadingOne")[0].style.display = "none"
        }
    }



}


function listen(){

    $("#carousel1")[0].addEventListener("touchstart", function(e){
        startx = e.touches[0].pageX;
        starty = e.touches[0].pageY;
    }, false);

    //手指离开屏幕
    $("#carousel1")[0].addEventListener("touchend", function(e) {
        var endx, endy;
        endx = e.changedTouches[0].pageX;
        endy = e.changedTouches[0].pageY;
        var direction = getDirection(startx, starty, endx, endy);
        switch (direction) {
            // case 0:
            //     alert("点击！");
            //     break;
            case 1:
                removedialog()
                break;
            // case 2:
            //     alert("向下！");
            //     break;
            case 3:
                rtbt()
                break;
            case 4:
                lfbt()
                break;
            // default:
            //     $(this).remove();
            //     console.log("remove")
            //     coverLayer(0);
            //     break;
        }
    }, true);
    $(".imgct1")[0].onmousedown=function(e){
        var a1=e.screenX;
        var a3=e.screenY
        firstTime = new Date().getTime();
        event.stopPropagation();
        $(".imgct1")[0].onmouseup=function (e){
            var a2=e.screenX;
            var a4= e.screenY
            lastTime = new Date().getTime();
            if( (lastTime - firstTime) < 120){
                key = true;
            }else {
                if (a1 > a2) {
                    if(a3-a4>200){
                        removedialog()
                    }else {
                        rtbt()
                    }

                }
                if (a1 < a2) {
                    if(a3-a4>200){
                        removedialog()
                    }else {
                        lfbt()
                    }

                }
            }
        }
    }
    $("#carousel1")[0].onwheel=function (e){
        if(e.wheelDelta < 0){
            rtbt()
        }else {
            lfbt()
        }
    }
    // $("#leftArrow").click(function () {
    //     event.stopPropagation();
    //     lfbt()
    // });
    // $("#rightArrow").click(function () {
    //     event.stopPropagation();
    //     rtbt()
    //
    // });
}

window.onscroll= function (){
    if(window.location.href.indexOf("#menu")!=-1){
        window.location.href= '#0'
    }
}

function tiao(){
    var form = $('#search').val()
    $.ajax({
        url:"/id/"+form,
        type:"POST",
        cache:false,
        success:function (result){
            console.log(result)
            if(result!=0){
                window.location.href="/id/"+form
            }else {
                $.ajax({
                    url:"/bookmark/"+form,
                    type:"PUT",
                    data: {"page":1},
                    cache:false,
                    success:function (result){
                        if(result!=0){
                            window.location.href="/bookmark/"+form
                        }else {
                            alert("没找到插画")
                            return false
                        }
                    }
                })

            }
        }

    });

    return false
}
function animate(ele, target) {
    //要用定时器，先清除定时器
    //一个盒子只能有一个定时器，这样的话，不会和其他盒子出现定时器冲突
    //我们可以把定时器本身，当成为盒子的一个属性
    clearInterval(ele.timer);
    //我们要求盒子既能向前又能向后，那么我们的步长就得有正有负
    //目标值如果大于当前值取正，目标值如果小于当前值取负
    var speed = target > ele.offsetLeft ? 10 : -10;  //speed指的是步长
    var totalval = Math.abs((target - ele.offsetLeft)/200)
    ele.timer = setInterval(function () {
        //在执行之前就获取当前值和目标值之差
        var val = target - ele.offsetLeft;
        //移动的过程中，如果目标值和当前值之差如果小于步长，那么就不能在前进了
        //因为步长有正有负，所有转换成绝对值来比较
        if (Math.abs(val) < Math.abs(speed*totalval)) {  //如果val小于步长，则直接到达目的地；否则，每次移动一个步长
            ele.style.left = target + "px";
            clearInterval(ele.timer);
        } else {
            ele.style.left = ele.offsetLeft + speed*totalval + "px";
        }
    }, 30)
}
function logout(){
    $.ajax({
        url:"/logout",
        type:"DELETE",
        cache:false,
        success:function (result){
            console.log(result)
            if(result!=0){
                alert("已经退出")
            }else {
                alert("出错")
                return false
            }
        }

    });

    return false
}
// window.onscroll= function(){
//   //文档内容实际高度（包括超出视窗的溢出部分）
//   var scrollHeight = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
//   //滚动条滚动距离
//   var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
//   //窗口可视范围高度
//   var clientHeight = window.innerHeight || Math.min(document.documentElement.clientHeight,document.body.clientHeight);
//
//
//
//
//   if(clientHeight + scrollTop >= scrollHeight){
//     if(document.readyState=='complete' && flag == 1){
//         $(".loadingOne")[0].style.display = "block"
//         flag = 0
//         var time = setInterval(function (){
//           var total = $(".item").length
//           console.log(total)
//           if (total==15*(page+1)){
//             var id3 = $('#'+'img' + total)[0]
//
//             id3.onload = function (){
//               console.log("加载完了")
//               waterFall()
//               flag = 1
//               $(".loadingOne")[0].style.display = "none"
//
//             }
//             clearInterval(time)
//           }
//         },100)
//     }
//   }
//
// }

function reflag(){
    // setInterval(reflag,3000)
    flag = 1
    console.log('reflag=1')
}
//初始化
window.onresize = function() {
    // 重新定义瀑布流
    if($("#carousel1").length == 0) {
        waterFall(-1);
    }else {
        console.log($(window).width())
        removedialog()
        bigImg(curimg)
    }
};
window.onload = function (){

    console.log("onload")
}
$().ready(function(){

})