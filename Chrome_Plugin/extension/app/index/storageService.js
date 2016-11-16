'use strict';

angular.module('myApp').service('storageService', function () {
    var _this = this;
    _this.emissionState = undefined;
    _this.timeResolution = undefined;

    this.setEmissionState = function (state) {
        _this.emissionState = state;
        this.sync();
    };

    this.setTimeRes = function (time) {
        _this.timeResolution = time;
        this.sync();
    };

    this.getEmissionState = function (cb) {
        chrome.storage.sync.get('emissionState', function (key) {
            if (key.emissionState) {
                _this.emissionState = key.emissionState;
            } else {
                _this.emissionState = "START";
                _this.sync();
            }
            cb(_this.emissionState);
        });
    };

    this.getTimeResolution = function (cb) {
        chrome.storage.sync.get('timeResolution', function (keys) {
            if (keys.timeResolution != null) {
                _this.timeResolution = keys.timeResolution;
            }
            else {
                _this.timeResolution = 1;
                _this.sync();
            }
            cb(_this.timeResolution)
        });
    };

    this.sync = function () {

        var obj = {
            'emissionState': _this.emissionState ,
            'timeResolution': _this.timeResolution
        };
        
        chrome.storage.sync.set(obj, function() {
        });
    };
});
