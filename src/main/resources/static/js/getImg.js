



function getImage(id){
    console.log(id,"로 호출됨.")
    const url="/webfist/getimg?id="+id
    imgbox = document.getElementById("imgbox");
    $.ajax({
        type: "GET",
        url: url,
        enctype: "multipart/form-data",
        xhrFields: { responseType: "blob" },
        crossDomain: true,
        success: function (data) {
            console.log("성공")

            let url = URL.createObjectURL(data);
            console.log(url)
            imgbox.src = url;
            imgbox.offsetWidth= "400px";
            imgbox.onload = function() {
                //cleanup.
                URL.revokeObjectURL(this.src);
            }
        },
        error: function (request, status, error) {
            console.log(
                "request : ",
                request,
                "status : ",
                status,
                "error : ",
                error
            );
        },
    });
}

function getImageList(id){
    const url="/analysis/getimg?id="+id
    imgbox = document.getElementsByClassName("imgbox");
    $.ajax({
        type: "GET",
        url: url,
        crossDomain: true,
        success: function (data) {
            console.log("성공" , data.length, typeof data[0])
            console.log(data[0])
            for(i=0; i<data.length;i++){
                let url = URL.createObjectURL(data[i]);
                imgbox[i].offsetWidth= "400px";
                imgbox[i].offsetWidth=imgbox.onload = function() {
                    //cleanup.
                    URL.revokeObjectURL(this.src);
                }
            }
        },
        error: function (request, status, error) {
            console.log(
                "request : ",
                request,
                "status : ",
                status,
                "error : ",
                error
            );
        },
    });
}