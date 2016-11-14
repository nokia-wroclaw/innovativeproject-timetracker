'use strict';

angular.module('index').service('storageService', function () {
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
