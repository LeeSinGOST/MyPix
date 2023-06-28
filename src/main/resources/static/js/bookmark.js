function bookmark(test,idd){
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: {"id":test, "flag":"2"},
        success:function (result){
            var r1 = String(JSON.parse(result).error)
            if(r1 === "false"){
                // console.log(idd)
                // $('#'+idd)[0].innerHTML("<div>  <svg t=\"1672671418522\" class=\"icon\"  viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"1381\" width=\"20\" height=\"20\"><path d=\"M760.384 64c47.808 0 91.968 11.968 132.352 35.84a264.32 264.32 0 0 1 95.872 97.152A263.68 263.68 0 0 1 1024 330.88c0 34.752-6.592 68.544-19.712 101.312a262.4 262.4 0 0 1-57.536 87.424L512 960 77.248 519.68A268.8 268.8 0 0 1 0 330.88c0-48.384 11.776-93.056 35.392-133.952A264.32 264.32 0 0 1 131.2 99.84 255.296 255.296 0 0 1 263.68 64 260.736 260.736 0 0 1 449.92 142.208l62.08 62.912 62.144-62.912a258.944 258.944 0 0 1 86.336-58.24A259.584 259.584 0 0 1 760.512 64h-0.128z\" fill=\"#d81e06\" p-id=\"1382\"></path></svg></div>");
                var hear = $('#heart'+idd)[0]
                hear.style.backgroundPosition= "right";
                hear.style.transition = "background .6s steps(28)";
                console.log("已收藏")
            }else {
                console.log("出现错误:"+result)
            }
        }
    })
}