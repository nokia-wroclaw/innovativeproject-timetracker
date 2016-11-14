'use strict';
//jak to podzielic
angular.module('index', []).controller('sendingStateController', function ($scope, storageService, $http) {

    $scope.storageService = storageService;

    $scope.$watch('storageService.sendingStateText', function() {
        $scope.sendingStateText = $scope.storageService.sendingStateText;
    });

    $scope.$watch('storageService.timeResolution', function() {
        $scope.timeResolution = $scope.storageService.timeResolution;
    });

    $scope.storageService.getSendingStateText(function(text){
        $scope.sendingStateText = text;
        $scope.$apply();
    });

    $scope.storageService.getTimeResolution(function(time){
        $scope.timeResolution = time;
        $scope.$apply();
    });

    $scope.changeSendingState = function() {
        //todo refractor: some loop; resolution;
        if ($scope.sendingStateText == 'START') {
            $http({
                method: "POST",
                url: "http://localhost:9000/user",
                params: {surname: "user", name: "pwd", login: "bka", email: "bla", password: "asd"}
            }).then(function mySucces(response) {
                storageService.sendingStateText = 'END';
                storageService.sync();
            }, function myError(response) {
                storageService.sendingStateText = 'connection error';
            });
        }
        else {
            storageService.sendingStateText = 'START';
            storageService.sync();
            //przestaje wysyłać
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