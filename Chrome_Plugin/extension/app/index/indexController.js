'use strict';

var timer = 0;
var timerDelay = 60000;
angular.module('myApp', [])
    .controller('TrackingStateController', function ($scope, storageService, $http) {

        $scope.loginError = false;
        $scope.workedToday = {
            hours: 0,
            minutes: 0
        };
        $scope.workedThisMonth = {
            hours: 0,
            minutes: 0
        };

        var fetchWorkedTime = function () {
            /*$http({
                //TODO
            }).then(function (response) {
                //TODO
            }, function (response) {
                console.error("Worked time fetching error", response);
            });*/
            //example
            $scope.workedToday.hours = ("00" + 5).slice(-2);
            $scope.workedToday.minutes = ("00" + 5).slice(-2);
            $scope.workedThisMonth.hours = ("00" + 25).slice(-2); //to change (three digits)
            $scope.workedThisMonth.minutes = ("00" + 5).slice(-2);
            if ($scope.isTracking)
                timer = setInterval(updateTimer, timerDelay);
            $scope.$apply();
        };

        var loadStorage = function () {
            storageService.getStorage(function (keys) {
                $scope.isTracking = keys.isTracking;
                $scope.isLogged = keys.isLogged;
                $scope.login = keys.login;
                $scope.selectedReminder = {id: keys.reminder};
                $scope.$apply();
            })
        };

        var updateTimer = function () {
            $scope.workedToday.minutes += 1;
            if ($scope.workedToday.minutes == 60) {
                $scope.workedToday.minutes = 0;
                $scope.workedToday.hours += 1;
            }
            $scope.workedThisMonth.minutes += 1;
            if ($scope.workedThisMonth.hours == 60) {
                $scope.workedThisMonth.minutes = 0;
                $scope.workedThisMonth.hours += 1;
            }
            $scope.$apply();
        };

        loadStorage();
        setTimeout(fetchWorkedTime, 100);

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
                clearInterval(timer);
            } else {
                port.postMessage({
                    isTracking: true
                });
                $scope.isTracking = true;
                timer = setInterval(updateTimer, timerDelay);
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
                clearInterval(timer);
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
                if ($scope.isTracking)
                    timer = setInterval(updateTimer, timerDelay);
                else
                    clearInterval(timer);
                $scope.$apply();
            }
        });
    });