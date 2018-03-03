$(document).ready(function () {
    $("#btnSubmit").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        doAjaxUploadFile();
    });

});

function doAjaxUploadFile() {

    // Get form
    var form = $('#fileUploadForm')[0];
    var data = new FormData(form);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "http://serveurpi.ddns.net:8080/MBAL/api/user/setProfilePicture?session_id="+session_id,
        headers: {
            "Authorization":"Bearer "+token,
        },
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (data) {
            $("#result").text(data);
        },
        error: function (e) {
            $("#result").text(e.responseText);
        }
    });
}

function doAjaxToken(password) {
    $.ajax({
        type: "POST",
        url: "http://serveurpi.ddns.net:8080/MBAL/oauth/token?grant_type=password&username=admin&password="+password,
        headers: {
          "Authorization":"Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0",
        },
        success: function (data) {
            token = data.access_token;
            setTokenFetched(true);
            $("#result").text(token);
        },
        error: function (e) {
            $("#result").text(e.responseText);
        }
    });
}

function doAjaxCreate(mail,password) {
    $.ajax({
        type: "POST",
        url: "http://serveurpi.ddns.net:8080/MBAL/api/user/create?name=test&prenom=test&mail="+mail+"&password="+password+"&num_tel=&role=USER",
        headers: {
            "Authorization":"Bearer "+token,
        },
        success: function (data) {
            if(data == "OK"){
                $("#result").text(data);
            }
        },
        error: function (e) {
            $("#result").text(e.responseText);
        }
    });
}

function doAjaxDelete(mail) {
    $.ajax({
        type: "POST",
        url: "http://serveurpi.ddns.net:8080/MBAL/api/user/deleteUser?username="+mail,
        headers: {
            "Authorization":"Bearer "+token,
        },
        success: function (data) {
            if(data == "OK"){
                $("#result").text("Deleted");
            }else{
                $("#result").text("Not deleted");
            }
        },
        error: function (e) {
            $("#result").text(e.responseText);
        }
    });
}