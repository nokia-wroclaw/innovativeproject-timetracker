'use strict';

var tracking = null;
var intervalTime = 15000;
var sendingParams = {
    login: "",
    password: "",
    date: 0,
    state: "End"
};
var isTracking = null;
var port = null;
angular.module('myApp').service('trackingService', function ($http, storageService) {

    this.setPort = function (newPort) {
        port = newPort;
        port.onMessage.addListener(function (message) {
            var keys = Object.keys(message);
            if (keys.includes("isTracking")) {
                isTracking = message.isTracking;
                storageService.updateStorage({
                    isTracking: message.isTracking
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

    var updateUserState = function () {
        storageService.getStorage(function (keys) {
            isTracking = keys.isTracking;
            sendingParams.login = keys.login;
            sendingParams.password = keys.password;
        })
    };

    this.startService = function () {
        updateUserState();
        setTimeout(function () {
            if (isTracking) {
                sendingParams.state = "Continue";
                track();
                tracking = setInterval(track, intervalTime);
            }
        }, 200);
    };

    this.changeTrackingState = function (state) {
        isTracking = state;
        storageService.updateStorage({
            isTracking: state
        });
        changeTracking();
    };

    var changeTracking = function () {
        if (isTracking) {
            if (tracking == null) {
                sendingParams.state = "Continue";
                track();
                tracking = setInterval(track, intervalTime);
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

    var track = function () {
        sendingParams.date = Date.now();
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
