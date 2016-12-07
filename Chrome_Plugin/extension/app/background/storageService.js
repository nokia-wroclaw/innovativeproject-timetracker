'use strict';

angular.module('myApp').service('storageService', function () {
    var storageElements = {
        emissionState: "START",
        login: "",
        password: "",
        isLogged: false
    };

    var loadStorage = function() {
        chrome.storage.sync.get(null, function (keys) {
            if (keys.emissionState)
                storageElements.emissionState = keys.emissionState;
            if (keys.login)
                storageElements.login = keys.login;
            if (keys.password)
                storageElements.password = keys.password;
            if (keys.isLogged)
                storageElements.isLogged = keys.isLogged;
        });
    };

    loadStorage();

    this.updateStorage = function (changes) {
        var keys = Object.keys(changes);
        if (keys.includes("emissionState"))
            storageElements.emissionState = changes.emissionState;
        if (keys.includes("login"))
            storageElements.login = changes.login;
        if (keys.includes("password"))
            storageElements.password = changes.password;
        if (keys.includes("isLogged"))
            storageElements.isLogged = changes.isLogged;
        this.sync();
    };

    this.getStorage = function (cb) {
        chrome.storage.sync.get(null, function (keys) {
            cb(keys);
        })
    };

    this.sync = function () {
        chrome.storage.sync.set(storageElements, function () {
        });
    };
});
