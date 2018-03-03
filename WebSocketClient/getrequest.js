$( document ).ready(function() {

  //  var url = window.location;

    // GET REQUEST
    $("#btnGetFiles").click(function(event){
        event.preventDefault();
        ajaxGetProfilePicture();
    });

    function ajaxGetProfilePicture() {
        if (session_id != null) {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/api/user/getPathProfilePicture/" + session_id,
                headers: {
                    "Authorization": "Bearer " + token,
                },
                success: function (data) {
                    document.getElementById('something').innerHTML = "";
                    var img = $("<img />").attr('src', 'http://localhost:8080/api/user/files/' + data)
                        .on('load', function () {
                            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                                alert('broken image!');
                            } else {
                                $("#something").append(img);
                            }
                        });
                },
                error: function (e) {
                    console.log(e.responseText);
                    //   $("#result").html(e.responseText);
                }
            });
        }
    }
});

function ajaxGetUsers(familyname){
    $.ajax({
        type : "GET",
        url: "http://localhost:8080/api/user/getUsersByFamilyName/"+familyname,
        headers: {
            "Authorization":"Bearer "+token,
        },
        success: function(data){
            $("#result").html("");
            $("#result").text(JSON.stringify(data));
        },
        error : function(e) {
            $("#result").html(e.responseText);
        }
    });
}

function ajaxGetSessionId(username){
    $.ajax({
        type : "GET",
        url: "http://localhost:8080/api/user/getSessionIdByUsername/"+username+"/",
        headers: {
            "Authorization":"Bearer "+token,
        },
        success: function(data){
            session_id = data;
            $("#result").html("");
            $("#result").text(data);
        },
        error : function(e) {
            $("#result").html(e.responseText);
        }
    });
}