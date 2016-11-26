'use strict';

angular.module('myApp', [])
    .controller('EmitStateController', function ($scope, storageService) {

        $scope.options = {
            //timeResolution: "",
            login: "",
            password: "",
            isLogged: ""
        };

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
            //only storage, todo check if in database
            $scope.options.isLogged = true;
            storageService.setLoginAndPassword($scope.options.login, $scope.options.password, true);
            chrome.runtime.sendMessage({"Login": $scope.options.login, "Password": $scope.options.password});
        };

        $scope.logout = function () {
            setEmissionState('START');
            $scope.options.isLogged = false;
            storageService.setLoginAndPassword("", "", false);
            chrome.runtime.sendMessage({"Logout": ""});
        };

        $scope.redirect = function () {
            var newURL = "http://localhost:9000/users";
            chrome.tabs.create({url: newURL});
        };
    });
