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
angular.module('myApp').service('trackingService', function ($http, storageService) {

    var getEmissionState = function () {
        storageService.getEmissionState(function (state) {
            emissionState = state;
        });
    };

    var getLogin = function () {
        storageService.getLogin(function (storageLogin) {
            sendingParams.login = storageLogin;
        });
    };

    var getPassword = function () {
        storageService.getPassword(function (storagePassword) {
            sendingParams.password = storagePassword;
        });
    };

    this.startService = function () {
        getLogin();
        getEmissionState();
        getPassword();
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

    this.processMessage = function (message) {
        var key = Object.keys(message)[0];
        switch (key) {
            case "EmissionState":
                emissionState = message.EmissionState;
                changeTracking();
                break;
            case "Login":
                sendingParams.login = message.Login;
                sendingParams.password = message.Password;
                break;
            default:
                console.log("Wrong key", key);
        }
    };

    var changeTracking = function () {
        if (emissionState == "END") {
            sendingParams.state = "Start";
            tracking = setInterval(track, intervalTime);
            setTimeout(function () {
                sendingParams.state = "Continue";
            }, 2000);

        } else {
            if (sendingParams.state != "End") {
                clearInterval(tracking);
                sendingParams.state = "End";
                track();
            }
        }
    };

    var getCurrentDate = function () {
        var currentDate = new Date();
        return currentDate.getDate() + "/"
            + (currentDate.getMonth() + 1) + "/"
            + currentDate.getFullYear() + "@"
            + currentDate.getHours() + ":"
            + currentDate.getMinutes();
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
