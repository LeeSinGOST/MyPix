page = 2;
function debounce(fn, timeout) {
    /*核心技术介绍
    1. 函数防抖需要使用定时器，但是定时器id不能是局部的  （局部变量函数执行完毕会被回收）
    2. 定时器id如果使用全局变量存储，则会造成全局变量污染
    3. 解决方案 ：
         (1)使用闭包延长局部变量声明周期，但是语法过于繁琐
         (2)利用函数本身也是对象，使用函数本身的静态成员来存储定时器id
    */

    //1.先清除上一次触发事件的定时器
    clearTimeout(debounce.timeID);
    //2.以最后一次触发为准
    debounce.timeID = setTimeout(fn, 500);
};
function load(){
    var url = window.location.pathname
    $.ajax({
        url: url,
        type: 'PUT',
        data:{"page":page},
        success:function (result){
            console.log(result)
            var html = [];
            for(var i=0; i<result.length; i++) {
                let works  = []
                let fol
                if(result[i].follow==='true'){
                    fol = '<button class="folbt">已关注</button>'
                }else {
                    fol = '<button class="folbt1" onclick="tiao1('+result[i].uid+',this)|}">关注</button>'
                }
                let wsize = result[i].works.length
                for(let k=1;k<wsize+1;k++){
                    works.push('<div class="foote"><div class="image'+k+' img"><img class="imgg" src="/img/c/540x540_70/'+result[i].works[k-1].url+'"></div></div>')
                }
                html.push('<div class="flame"><div class="item" onclick="tiao('+result[i].uid+')"><div class="header">' +
                    '<div  class="profile"><img src="/img/'+result[i].profileimg+'"></div>' +
                    '<div class="username"><p>'+result[i].username+'</p></div>' +
                    '<div class="follow">'+fol+'</div>'+
                    '<div class="discript"><p>'+result[i].comment+'</p></div></div><div class="footer">'+
                    works.join("")+
                    '</div></div></div>')
            }
            $(".bigbox").append(html.join(""))
            $(".loadingOne")[0].style.display = "none"
            page = page+1
        }
    })
}
function tiao(id){
    window.location.href="/id/"+id
}
function tiao2(){
    var form = $('#search').val()
    $.ajax({
        url:"/id/"+form,
        type:"POST",
        cache:false,
        success:function (result){
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
function tiao1(id,data){
    event.stopPropagation();
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: {"id":id, "flag":"1"},
        success:function (result){
            console.log("res:"+result)
            if(result == 1){
                data.innerText="已关注"
                data.removeAttribute("onclick")
                console.log("已关注")
            }else {
                console.log("出现错误")
            }
        }
    })
}
window.onscroll= function(){
    //文档内容实际高度（包括超出视窗的溢出部分）
    var scrollHeight = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
    //滚动条滚动距离
    var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    //窗口可视范围高度
    var clientHeight = window.innerHeight || Math.min(document.documentElement.clientHeight,document.body.clientHeight);

    if(clientHeight + scrollTop >= scrollHeight){
        $(".loadingOne")[0].style.display = "block"
        // console.log("触底")
        // // load()
            debounce(load.bind(this),500)
    }

}