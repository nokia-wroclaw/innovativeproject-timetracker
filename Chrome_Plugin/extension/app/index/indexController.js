'use strict';

angular.module('myApp', [])
    .controller('EmitStateController', function ($scope, storageService, $http) {

        $scope.options = {
            //timeResolution: "",
            login: "",
            password: "",
            isLogged: "",
        };

        $scope.loginError = false;

        $scope.getEmissionState = function () {
            storageService.getEmissionState(function (emissionState) {
                $scope.emissionState = emissionState;
                $scope.$apply();
            });
        };

        /*$scope.getTimeResolution = function () {
         storageService.getTimeResolution(function (timeResolution) {
         $scope.options.timeResolution = timeResolution;
         $scope.$apply();
         });
         };*/

        $scope.getLoggedState = function () {
            storageService.getLoggedState(function (isLogged) {
                $scope.options.isLogged = isLogged;
            });
        };

        $scope.getLogin = function () {
            storageService.getLogin(function (login) {
                $scope.options.login = login;
                $scope.$apply();
            });
        };

        $scope.getEmissionState();
        //$scope.getTimeResolution();
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

        /*$scope.setTimeResolution = function () {
         storageService.setTimeResolution($scope.options.timeResolution);
         chrome.runtime.sendMessage({"TimeResolution": $scope.options.timeResolution});
         };*/

        $scope.login = function () {
            $http({
                    method: "POST",
                    url: "http://localhost:9000/loginextension",
                    data: {
                        login: $scope.options.login,
                        password: $scope.options.password
                    }
                }
            ).then(function (response) {
                if (response.data != "ERROR- Login not found") {
                    $scope.options.isLogged = true;
                    $scope.loginError = false;
                    storageService.setLoginAndPassword($scope.options.login, $scope.options.password, true);
                    chrome.runtime.sendMessage({"Login": $scope.options.login, "Password": $scope.options.password});
                } else {
                    $scope.loginError = true;
                }
            }, function (response) {
                console.error("Login connection error", response);
            });
        };

        $scope.logout = function () {
            $http({
                    method: "POST",
                    url: "http://localhost:9000/logoutextension",
                    data: {
                        login: sendingParams.login,
                        password: sendingParams.password
                    }
                }
            ).then(function (response) {
                setEmissionState('START');
                $scope.options.isLogged = false;
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
