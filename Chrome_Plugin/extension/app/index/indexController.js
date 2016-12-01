'use strict';

angular.module('myApp', [])
    .controller('EmitStateController', function ($scope, storageService, $http) {

        $scope.loginError = false;

        $scope.getEmissionState = function () {
            storageService.getEmissionState(function (emissionState) {
                $scope.emissionState = emissionState;
                $scope.$apply();
            });
        };

        $scope.getLoggedState = function () {
            storageService.getLoggedState(function (isLogged) {
                $scope.isLogged = isLogged;
            });
        };

        $scope.getLogin = function () {
            storageService.getLogin(function (login) {
                $scope.login = login;
                $scope.$apply();
            });
        };

        $scope.getEmissionState();
        $scope.getLoggedState();
        $scope.getLogin();

        $scope.changeEmissionState = function () {
            if ($scope.emissionState == 'START') {
                setEmissionState('END');
            } else {
                setEmissionState('START');
            }
        };

        var setEmissionState = function (state) {
            storageService.setEmissionState(state);
            $scope.emissionState = state;
            chrome.runtime.sendMessage({"EmissionState": state});
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
                    storageService.setLoginAndPassword($scope.login, $scope.password, true);
                    chrome.runtime.sendMessage({"Login": $scope.login, "Password": $scope.password});
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
                setEmissionState('START');
                $scope.isLogged = false;
                storageService.setLoginAndPassword("", "", false);
            }, function (response) {
                console.error("Logout connection error", response);
            });
        };

        $scope.redirect = function () {
            var newURL = "http://localhost:9000";
            chrome.tabs.create({url: newURL});
        };
    });
