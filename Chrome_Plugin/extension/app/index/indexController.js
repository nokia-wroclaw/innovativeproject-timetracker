'use strict';

angular.module('index', []).controller('sendingStateController', function ($scope, storageService,
                                                                           $http, $interval, serverService) {

    $scope.storageService = storageService;
    $scope.serverService = serverService;

    $scope.$watch('storageService.sendingStateText', function() {
        $scope.sendingStateText = $scope.storageService.sendingStateText;
    });

    $scope.$watch('storageService.timeResolution', function() {
        $scope.timeResolution = $scope.storageService.timeResolution;
    });

    $scope.storageService.getSendingStateText(function(text){
        $scope.sendingStateText = text;
        $scope.$apply();
        //wywolanie servicu
    });

    $scope.storageService.getTimeResolution(function(time){
        $scope.timeResolution = time;
        $scope.$apply();
    });

    var start;
    $scope.changeSendingState = function() {
        if ($scope.sendingStateText == 'START') {
            $scope.serverService.repeatSend(500);
            /*$http({
                method: "POST",
                url: "http://localhost:9000/user",
                //url: "http://localhost:9000/login",
                params: {surname: "user", name: "pwd", login: "bka", email: "bla", password: "asd"}
                //params: {name: "pwd", surname: "user"}
            }).then(function mySucces(response) {
                storageService.sendingStateText = 'END';
                storageService.sync();
            }, function myError(response) {
                storageService.sendingStateText = 'connection error';
            });*/
            /*start = $interval(function() {
                $http({
                    method: "POST",
                    url: "http://localhost:9000/user",
                    //url: "http://localhost:9000/login",
                    params: {surname: "user", name: "pwd", login: "bka", email: "bla", password: "asd"}
                    //params: {name: "pwd", surname: "user"}
                }).then(function mySucces(response) {
                    storageService.sendingStateText = 'END';
                    storageService.sync();
                }, function myError(response) {
                    storageService.sendingStateText = 'connection error';
                })
            }, 500);*/
        }
        else {
            storageService.sendingStateText = 'START';
            storageService.sync();
            //przestaje wysyłać
            $interval.cancel(start);
        }
    };

    $scope.syncTimeResolution = function () {
        storageService.timeResolution = $scope.timeResolution;
        storageService.sync();
    };

    $scope.redirect = function() {
        var newURL = "http://localhost:9000/users";
        chrome.tabs.create({ url: newURL });
    }
});