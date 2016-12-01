'use strict';

angular.module('myApp').service('storageService', function () {
    var _this = this;
    _this.emissionState = undefined;
    _this.login = undefined;
    _this.password = undefined;
    _this.isLogged = false;

    this.setEmissionState = function (state) {
        _this.emissionState = state;
        this.sync();
    };

    this.setLoginAndPassword = function (login, password, isLogged) {
        _this.login = login;
        _this.password = password;
        _this.isLogged = isLogged;
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

    this.getLogin = function (cb) {
        chrome.storage.sync.get('login', function (keys) {
            if (keys.login != null) {
                _this.login = keys.login;
            }
            cb(_this.login);
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

    this.getLoggedState = function (cb) {
        chrome.storage.sync.get('isLogged', function (keys) {
            if (keys.isLogged != null) {
                _this.isLogged = keys.isLogged;
            }
            cb(_this.isLogged);
        });
    };

    this.sync = function () {
        var obj = {
            'emissionState': _this.emissionState,
            'login': _this.login,
            'password': _this.password,
            'isLogged': _this.isLogged
        };
        chrome.storage.sync.set(obj, function () {
        });
    };
});
