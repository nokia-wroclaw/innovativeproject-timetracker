'use strict';

var sending = null;
var defaultIntervalTime = 1500; //on production set 15000
angular.module('myApp').service('serverService', function ($http, storageService) {

    this.startService = function (intervalTime) {
        sending = setInterval(repeatSend, defaultIntervalTime);
    };

    this.stop = function () {
        clearInterval(sending);
    };

    var repeatSend = function () {
        storageService.getEmissionState(function (state) {
            if (state == "START") {
                stop();
            } else {
                send();
            }
        });
    };

    var send = function () {
        $http({
                  method: "POST",
                  url: "http://localhost:9000/addUser",
                  params: {
                      surname: "user",
                      name: "pwd",
                      login: "bka",
                      email: "bla",
                      password: "asd"
                  }
              }
        ).then(function mySuccess(response) {
        }, function myError(response) {
            console.error("Connection error", response);
        });
    };

    var getTimeInterval = function () {
        storageService.getTimeResolution(function (timeResolution) {
            $scope.timeResolution = timeResolution;
        });
    };
});
