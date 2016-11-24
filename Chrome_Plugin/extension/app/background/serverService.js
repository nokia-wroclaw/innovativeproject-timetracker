'use strict';

var sending = null;
var intervalTime = 1500; //on production set 15000
var sendingParams = { //todo change after server change
    surname: "surname",
    name: "name",
    login: "",
    email: "email",
    password: ""
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
        setTimeout(function(){
            if (emissionState == "END" && sendingParams.login != "") {
                /* after server change

                sendingParams.date = getCurrentDate();
                sendingParams.sendingState = "START" */

                sending = setInterval(send, intervalTime);
                //sendingParams.sendingState = "CONTINUE";
            }
        }, 2000);
    };

    this.processMessage = function (message) {
        var key = Object.keys(message)[0];
        switch (key) {
            case "EmissionState":
                emissionState = message.EmissionState;
                processChange();
                break;
            case "TimeResolution":
                //todo
                break;
            case "Login":
                sendingParams.login = message.Login;
                sendingParams.password = message.Password;
                processChange();
                break;
            default:
                console.log("Wrong key", key);
        }
    };

    var processChange = function () {
        if (sending != null)
            clearInterval(sending);
        if (emissionState == "START" && sendingParams.login != "") {
            //sendingParams.sendingState = "START";
            sending = setInterval(send, intervalTime);
            //sendingParams.sendingState = "CONTINUE";
        } else {
            //sendingParams.sendingState = "END";
            //send();
            console.log("sending stop");
        }
    };

    /*
    after server implementation

    var getCurrentDate = function () {
        var currentDate = new Date();
        return currentDate.getDate() + "/"
            + (currentDate.getMonth()+1)  + "/"
            + currentDate.getFullYear() + " @ "
            + currentDate.getHours() + ":"
            + currentDate.getMinutes();
    };*/

    var send = function () {
        $http({
                  method: "POST",
                  url: "http://localhost:9000/addUser",
                  params: sendingParams
              }
        ).then(function (response) {
            console.log("response success");
        }, function (response) {
            console.error("Connection error", response);
        });
    };
});
