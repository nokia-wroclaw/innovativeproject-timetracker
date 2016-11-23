'use strict';

angular.module('myApp').service('storageService', function () {
    var _this = this;
    _this.emissionState = undefined;
    _this.timeResolution = undefined;
    _this.email = undefined;
    _this.password = undefined;

    this.setEmissionState = function (state) {
        _this.emissionState = state;
        this.sync();
    };

    this.setTimeResolution = function (time) {
        _this.timeResolution = time;
        this.sync();
    };

    this.setEmailAndPassword = function (email, password) {
        _this.email = email;
        _this.password = password;
        this.sync();
    };

    this.getEmissionState = function (cb) {
        chrome.storage.sync.get('emissionState', function (keys) {
            console.log(keys);
            if (keys.emissionState) {
                _this.emissionState = keys.emissionState;
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
            cb(_this.timeResolution);
        });
    };

    this.getEmail = function (cb) {
        chrome.storage.sync.get('email', function (keys) {
            if (keys.email != null) {
                _this.email = keys.email;
            }
            console.log("returtning email", _this.email);
            cb(_this.email);
        });
    };

    this.getPassword = function (cb) {
        chrome.storage.sync.get('password', function (keys) {
            if (keys.password != null) {
                _this.password = keys.password;
            }
            cb(_this.password);
        });
    };

    this.sync = function () {
        var obj = {
            'emissionState': _this.emissionState ,
            'timeResolution': _this.timeResolution,
            'email': _this.email,
            'password': _this.password
        };
        
        chrome.storage.sync.set(obj, function() {
        });
    };
});
