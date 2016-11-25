/**
 * Created by Marko on 23.11.2016.
 */
var timerId;

function start(event) {
    var searchPropertiesWrapper = {}
    searchPropertiesWrapper["url"] = $("#url").val();
    searchPropertiesWrapper["word"] = $("#word").val();
    searchPropertiesWrapper["countOfThreads"] = $("#countOfThreads").val();
    searchPropertiesWrapper["countOfLinks"] = $("#countOfLinks").val();
    console.log(searchPropertiesWrapper);
    $.ajax({
        url: '/start',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        mimeType: 'application/json',
        data: JSON.stringify(searchPropertiesWrapper),
        success: function (response) {
            var result = response.msg
            timerId = setInterval(updateDatas, 500);
            $("#warning_text").text("");

        },
        error: function (textStatus, errorThrown) {
            $("#warning_text").text("write current values");
        }
    });
}

function updateDatas(event) {
    $.ajax({
        url: '/updateDatas',
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (response) {
            var result = response.msg;
            if (result == 'paused') {
                clearInterval(timerId);
            } else if (result == 'ok') {
                var loaded = response.loaded;

                $(".bar").css("width", "100%");
                $("#resul_load").text("100%");
                clearInterval(timerId);
                $("#result_text").text(response.result);
                for (var i = 1; i < 1000; i++) {
                    clearInterval(i);
                }
            } else {

                $("#result_text").text(result);
                if (loaded == "100%") {
                    clearInterval(timerId);
                }
                var loaded = response.loaded;

                $(".bar").css("width", loaded);
                $("#resul_load").text(loaded);
            }
        },
        error: function (textStatus, errorThrown) {

        }
    });
}

function stop(event) {
    $.ajax({
        url: '/stop',
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (response) {
            clearInterval(timerId);
            console.log('in success');
        },
        error: function (textStatus, errorThrown) {
            console.log(textStatus, errorThrown)

        }
    });
}
function pause(event) {
    $.ajax({
        url: '/pause',
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (response) {

        },
        error: function (textStatus, errorThrown) {
            console.log(textStatus, errorThrown)

        }
    });
}