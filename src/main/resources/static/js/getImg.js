



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

function getImageList(id,method){
    console.log(method)
    const url="/analysis/getimg?id="+id+"&method="+method
    imgbox = document.getElementById(method);
    console.log(imgbox)
    $.ajax({
        type: "GET",
        url: url,
        enctype: "multipart/form-data",
        xhrFields: { responseType: "blob" },
        crossDomain: true,
        success: function (data) {
            console.log("성공")
            let url = URL.createObjectURL(data);
            let len =
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