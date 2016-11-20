'use strict';

var sending = null;
var stateChecking = null;
var statusCheckTime = 1500;
var intervalTime = 1500; //on production set 15000
var loginParams = null;
angular.module('myApp').service('serverService', function ($http, storageService) {

    this.startService = function () {
        stateChecking = setInterval(checkState, statusCheckTime);
    };

    var stopServerSending = function () {
        clearInterval(sending);
    };

    var checkState = function () {
        var email = storageService.getEmail();
        var password = storageService.getPassword();
        var state = storageService.getEmissionStateVar();
        if (shouldSend(email, state)) {
            if (sending == null) {
                loginParams = {
                    surname: "surname",
                    name: "name",
                    login: "login",
                    email: email,
                    password: password
                };
                sending = setInterval(send, intervalTime);
            }
        } else {
            if (sending != null) {
                stopServerSending();
            }
        }
    };

    var shouldSend = function (email, state) {
        if (state == "END" && email != "") {
            return true;
        } else {
            return false;
        }
    };

    var send = function () {
        $http({
                  method: "POST",
                  url: "http://localhost:9000/addUser",
                  params: loginParams
              }
        ).then(function (response) {
            console.log("response success");
        }, function (response) {
            console.error("Connection error", response);
        });
    };
});
