'use strict';

var tracking = null;
var intervalTime = 1500; //on production set 15000
var sendingParams = {
    login: "",
    password: "",
    date: "",
    state: ""
};
var emissionState = null;
var port = null;
angular.module('myApp').service('trackingService', function ($http, storageService) {

    this.setPort = function (newPort) {
        port = newPort;
        port.onMessage.addListener(function (message) {
            var keys = Object.keys(message);
            if (keys.includes("emissionState")) {
                emissionState = message.emissionState;
                storageService.updateStorage({
                    emissionState: message.emissionState
                });
                changeTracking();
            }
            if (keys.includes("login")) {
                sendingParams.login = message.login;
                sendingParams.password = message.password;
                storageService.updateStorage({
                    login: message.login,
                    password: message.password,
                    isLogged: message.isLogged
                });
            }
        });
    };

    var getStorage = function () {
        storageService.getStorage(function (keys) {
            emissionState = keys.emissionState;
            sendingParams.login = keys.login;
            sendingParams.password = keys.password;
        })
    };

    this.startService = function () {
        getStorage();
        setTimeout(function () {
            if (emissionState == "END") {
                sendingParams.state = "Start";
                tracking = setInterval(track, intervalTime);
                setTimeout(function () {
                    sendingParams.state = "Continue";
                }, 2000);
            }
        }, 2000);
    };

    this.changeEmissionState = function (state) {
        emissionState = state;
        storageService.updateStorage({
            emissionState: state
        });
        changeTracking();
    };

    var changeTracking = function () {
        if (emissionState == "END") {
            if (tracking == null) {
                sendingParams.state = "Start";
                tracking = setInterval(track, intervalTime);
                setTimeout(function () {
                    sendingParams.state = "Continue";
                }, 2000);
            }

        } else {
            if (sendingParams.state != "End") {
                clearInterval(tracking);
                tracking = null;
                sendingParams.state = "End";
                track();
            }
        }
    };

    var getCurrentDate = function () {
        var currentDate = new Date();
        var day = currentDate.getDate();
        day = (day < 10) ? '0' + day: day;
        var month = currentDate.getMonth() + 1;
        month = (month < 10) ? '0' + month: month;
        var hour = currentDate.getHours();
        hour = (hour < 10) ? '0' + hour: hour;
        var minutes = currentDate.getMinutes();
        minutes = (minutes < 10) ? '0' + minutes: minutes;
        return day + "/" + month + "/" + currentDate.getFullYear() + "@" + hour + ":" + minutes;
    };

    var track = function () {
        sendingParams.date = getCurrentDate();
        console.log(sendingParams);
        $http({
                method: "POST",
                url: "http://localhost:9000/tracking",
                data: sendingParams
            }
        ).then(function (response) {
            console.log("tracking success", response);
        }, function (response) {
            console.error("tracking error", response);
        });
    };
});
