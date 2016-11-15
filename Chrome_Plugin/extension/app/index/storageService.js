'use strict';

angular.module('index').service('storageService', function () {
    var _this = this;
    _this.sendingStateText = undefined;
    _this.timeResolution = undefined;

    this.setSendingState  = function (state)  {
    _this.sendingStateText = state;
    }
    this.setTimeRes  = function (state)  {
        _this.timeResolution = state;
    }


    this.getSendingStateText = function(callback) {
        if (_this.sendingStateText) {
            return _this.sendingStateText;
        }
        try {

        chrome.storage.sync.get('sendingStateText', function(keys) {
            console.log(keys);
            if (keys.sendingStateText != null) {
                _this.sendingStateText = keys.sendingStateText;

                callback(_this.sendingStateText);
            }
            else {
                _this.sendingStateText = "START";

                callback(_this.sendingStateText);
            }
        });
        } catch(e){
            console.error(e);
        }

    };

    this.getTimeResolution = function(callback) {
        if(_this.timeResolution) {
            return _this.timeResolution;
        }
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
        console.log(_this.sendingStateText);

        chrome.storage.sync.set({
            sendingStateText: _this.sendingStateText,
            timeResolution: _this.timeResolution
        }, function() {
            console.log('Data is stored in Chrome storage');
            console.log(_this.sendingStateText);

        });
    };

    this.dupa = function () {
        console.log('dupa');
    }
});
