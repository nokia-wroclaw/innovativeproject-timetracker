'use strict';

angular.module('myApp', [])
    .controller('TrackingStateController', function ($scope, storageService, $http) {

        $scope.loginError = false;

        var loadStorage = function () {
            storageService.getStorage(function (keys) {
                $scope.isTracking = keys.isTracking;
                $scope.isLogged = keys.isLogged;
                $scope.login = keys.login;
                $scope.selectedReminder = {id: keys.reminder};
                $scope.$apply();
            })
        };

        loadStorage();

        $scope.reminderOptions = [
            {id: 5, name: '5 minutes'},
            {id: 10, name: "10 minutes"},
            {id: 15, name: "15 minutes"},
            {id: 30, name: "30 minutes"},
            {id: 60, name: "1 hour"},
            {id: 120, name: "2 hours"},
            {id: 0, name: "never"}
        ];

        $scope.changeReminderTime = function () {
            port.postMessage({
                reminder: $scope.selectedReminder.id
            });
        };

        $scope.changeTrackingState = function () {
            if ($scope.isTracking) {
                port.postMessage({
                    isTracking: false
                });
                $scope.isTracking = false;
            } else {
                port.postMessage({
                    isTracking: true
                });
                $scope.isTracking = true;
            }
        };

        $scope.signIn = function () {
            $http({
                    method: "POST",
                    url: "http://localhost:9000/loginextension",
                    data: {
                        login: $scope.login,
                        password: $scope.password
                    }
                }
            ).then(function (response) {
                if (response.data != "ERROR- Login not found") {
                    $scope.isLogged = true;
                    $scope.loginError = false;
                    port.postMessage({
                        login: $scope.login,
                        password: $scope.password,
                        isLogged: true
                    });
                } else {
                    $scope.loginError = true;
                }
            }, function (response) {
                console.error("Login connection error", response);
            });
        };

        $scope.signOut = function () {
            $http({
                    method: "POST",
                    url: "http://localhost:9000/logoutextension",
                    data: {
                        login: $scope.login,
                        password: $scope.password
                    }
                }
            ).then(function (response) {
                $scope.isTracking = false;
                $scope.isLogged = false;
                port.postMessage({
                    isTracking: false,
                    login: "",
                    password: "",
                    isLogged: false
                });
            }, function (response) {
                console.error("Logout connection error", response);
            });
        };

        $scope.redirect = function () {
            var newURL = "http://localhost:9000";
            chrome.tabs.create({url: newURL});
        };

        var port = chrome.extension.connect({
            name: "Popup-background communication"
        });

        port.onMessage.addListener(function (message) {
            if (Object.keys(message).includes("isTracking")) {
                $scope.isTracking = message.isTracking;
                $scope.$apply();
            }
        });
    });