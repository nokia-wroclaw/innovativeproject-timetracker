'use strict';

angular.module('myApp', []).controller('sendingStateController', function ($scope, extensionStorage) {

    $scope.extensionStorage = extensionStorage;

    $scope.$watch('extensionStorage.sendingStateText', function() {
        $scope.sendingStateText = $scope.extensionStorage.sendingStateText;
    });

    $scope.$watch('extensionStorage.timeResolution', function() {
        $scope.timeResolution = $scope.extensionStorage.timeResolution;
    });

    $scope.extensionStorage.getSendingStateText(function(text){
        $scope.sendingStateText = text;
        $scope.$apply();
    });

    $scope.extensionStorage.getTimeResolution(function(time){
        $scope.timeResolution = time;
        $scope.$apply();
    });

    $scope.changeSendingState = function() {
        if ($scope.sendingStateText == 'START')
            extensionStorage.sendingStateText = 'END';
        //przestaje wysylac
        else {
            extensionStorage.sendingStateText = 'START';
            //zaczyna wysylac
        }
        extensionStorage.sync();
    };

    $scope.syncTimeResolution = function () {
        extensionStorage.timeResolution = $scope.timeResolution;
        extensionStorage.sync();
    };
    
    $scope.redirect = function() {
        var newURL = "http://stackoverflow.com/";
        chrome.tabs.create({ url: newURL });
    }
});

angular.module('myApp').service('extensionStorage', function ($q) {
    var _this = this;

    this.getSendingStateText = function(callback) {
        chrome.storage.sync.get('sendingStateText', function(keys) {
            if (keys.sendingStateText != null) {
                _this.sendingStateText = keys.sendingStateText;
                callback(_this.sendingStateText);
            }
            else {
                _this.sendingStateText = "START";
                callback(_this.sendingStateText);
            }
        });
    };

    this.getTimeResolution = function(callback) {
        chrome.storage.sync.get('timeResolution', function(keys) {
            if (keys.timeResolution != null) {
                _this.timeResolution = keys.timeResolution;
                callback(_this.timeResolution);
            }
            else {
                _this.timeResolution = 1;
                callback(_this.timeResolution);
            }
        });
    };

    this.sync = function() {
        chrome.storage.sync.set({
            sendingStateText: this.sendingStateText,
            timeResolution: this.timeResolution
        }, function() {
            console.log('Data is stored in Chrome storage');
        });
    }
});