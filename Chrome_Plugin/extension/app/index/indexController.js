'use strict';

angular.module('myApp', [])
    .controller('EmitStateController', function ($scope, storageService, $http) {

        $scope.loginError = false;

        var getStorage = function () {
            storageService.getStorage(function (keys) {
                if(keys.emissionState == 'END') {
                    $('.switch').toggleClass("switchOn");
                }
                $scope.emissionState = keys.emissionState;
                $scope.isLogged = keys.isLogged;
                $scope.login = keys.login;
                $scope.$apply();
            })
        };

        getStorage();

        $scope.changeEmissionState = function () {
            if ($scope.emissionState == 'START') {
                port.postMessage({
                    "emissionState": 'END'
                });
                $scope.emissionState = 'END';
                $('.switch').toggleClass("switchOn");

            } else {
                port.postMessage({
                    "emissionState": 'START'
                });
                $scope.emissionState = 'START';
                $('.switch').toggleClass("switchOn");

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
                if($scope.emissionState != 'START'){
                    $('.switch').toggleClass("switchOn");
                }
                $scope.emissionState = 'START';
                $scope.isLogged = false;
                port.postMessage({
                    "emissionState": 'START',
                    "login": "",
                    "password": "",
                    "isLogged": false
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
            var keys = Object.keys(message);
            if (keys.includes("emissionState")){
                $('.switch').toggleClass("switchOn");
                $scope.emissionState = message.emissionState;
                $scope.$apply();
            }
        });
    });