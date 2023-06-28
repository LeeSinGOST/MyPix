function tiao3(todate){
    window.location.href = url+'?date='+todate
}
function getpic(){
    var total = $(".item").length
    page = page+1
    url = window.location.pathname
    $.ajax({
        url: url,
        type: 'PUT',
        data:{"page":page,"mode1":mode1,"date":date1},
        cache:false,
        success:function (result)
        {
            var html = [];
            for(var i=0; i<result.length; i++){

                array.push(result[i])
                var id1 = parseInt(total)+i+1
                var num = result[i].num
                var tem1 = '<div class="item"><div class="top"><p>'+result[i].title+'</p>'
                if(num>1){
                    tem1 = tem1 + '<div class="numtag"><span>'+num+'</span></div>'
                }
                html.push(tem1+'</div>  <div  class="imgct"><img '+'class="img" id=img'+id1+' data-src="/pid/'+result[i].pid+'" src="#" alt="'+result[i].num+'"></div>  <div id="heart'+id1+'" class="box"></div><div class="bottom"><a href="/id/'+result[i].uid+'" target="_blank"><p>'+result[i].username+'</p></a></div></div>')
            }
            $(".masonry").append(html.join(""))
            waterFall(cols)
            for(var j=0;j<result.length;j++){
                let num = parseInt(total)+j
                let id = "img"+num
                observer.observe($("#"+id)[0])
            }
        }

    });
}