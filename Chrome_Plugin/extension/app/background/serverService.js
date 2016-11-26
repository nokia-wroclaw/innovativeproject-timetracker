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
angular.module('myApp').service('serverService', function ($http, storageService) {

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
                sendingParams.date = getCurrentDate();
                sendingParams.sendingState = "Start";
                tracking = setInterval(track, intervalTime);
                sendingParams.sendingState = "Continue";
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
            /*case "TimeResolution":
             break;*/
            case "Login":
                sendingParams.login = message.Login;
                sendingParams.password = message.Password;
                login();
                break;
            case "Logout":
                logout();
                break;
            default:
                console.log("Wrong key", key);
        }
    };

    var changeTracking = function () {
        if (tracking != null)
            clearInterval(tracking);
        if (emissionState == "END") {
            sendingParams.sendingState = "Start";
            tracking = setInterval(track, intervalTime);
            sendingParams.sendingState = "Continue";
        } else {
            sendingParams.sendingState = "End";
            track();
            console.log("sending stop");
        }
    };

    var getCurrentDate = function () {
        var currentDate = new Date();
        return currentDate.getDate() + "/"
            + (currentDate.getMonth() + 1) + "/"
            + currentDate.getFullYear() + " @ "
            + currentDate.getHours() + ":"
            + currentDate.getMinutes();
    };

    var logout = function () {
        $http({
                method: "POST",
                url: "http://localhost:9000/logoutextension",
                params: {
                    login: sendingParams.login,
                    password: sendingParams.password
                }
            }
        ).then(function (response) {
            console.log("logout success", response);
        }, function (response) {
            console.error("Logout error", response);
        });
    };

    var login = function () {
        console.log(sendingParams.login, sendingParams.password);
        $http({
                method: "POST",
                url: "http://localhost:9000/loginextension",
                params: {
                    login: sendingParams.login,
                    password: sendingParams.password
                }
            }
        ).then(function (response) {
            console.log("login success", response);
        }, function (response) {
            console.error("Login error", response);
        });
    };

    var track = function () {
        $http({
                method: "POST",
                url: "http://localhost:9000/tracking",
                params: sendingParams
            }
        ).then(function (response) {
            console.log("tracking success", response);
        }, function (response) {
            console.error("tracking error", response);
        });
    };
});
