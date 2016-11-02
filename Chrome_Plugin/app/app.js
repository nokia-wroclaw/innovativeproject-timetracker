'use strict';

angular.module("myApp", [])
.controller("sendingStateController", function($scope) {
    $scope.sendingStateText = 'START';
    $scope.timeResolution = 1;
    $scope.changeSendingState = function() {
        if ($scope.sendingStateText == 'START')
            $scope.sendingStateText = 'END'
            //przestaje wysylac
        else {
            $scope.sendingStateText = 'START'
            //zaczyna wysylac
        }
    };
})

.controller("redirectController", function($scope) {
    $scope.redirect = function() {
        var newURL = "http://stackoverflow.com/";
        chrome.tabs.create({ url: newURL });
    }
})