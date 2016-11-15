'use strict';

angular.module('index', []).controller('sendingStateController', function ($scope, storageService, serverService) {
    var sendTime = 1500;

    $scope.storageService = storageService;
    $scope.serverService = serverService;

    $scope.storageService.getSendingStateText(function(text){
        $scope.sendingStateText = text;
        $scope.$apply();
        console.log("start");
        if ($scope.sendingStateText == 'END')
            $scope.serverService.repeatSend(sendTime);
    });

    $scope.storageService.getTimeResolution(function(time){
        $scope.timeResolution = time;
        $scope.$apply();
    });

    $scope.changeSendingState = function() {
console.log('czesssc');
        if ($scope.sendingStateText == 'START') {
            $scope.storageService.setSendingState('END');
            $scope.storageService.sync();
            $scope.sendingStateText = 'END';
            $scope.serverService.repeatSend(sendTime);
        }
        else {
            $scope.serverService.stop();
            $scope.sendingStateText = 'START';
            $scope.storageService.setSendingState('START');
            $scope.storageService.sync();
            $scope.storageService.dupa();

        }
    };

    $scope.syncTimeResolution = function () {
        storageService.setTimeRes($scope.timeResolution);
        storageService.sync();
    };

    $scope.redirect = function() {
        var newURL = "http://localhost:9000/users";
        chrome.tabs.create({ url: newURL });
    }
});