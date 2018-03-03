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
        url: "http://localhost:8080/api/user/setProfilePicture?session_id="+session_id,
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
        url: "http://localhost:8080/oauth/token?grant_type=password&username=admin&password="+password,
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