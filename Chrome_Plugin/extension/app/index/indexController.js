'use strict';

angular.module('myApp', [])
    .controller('EmmitStateController', function ($scope, storageService) {

        $scope.options = {
            timeResolution: "",
            email: "",
            password: ""
        };

        $scope.items = ['Home', 'Options'];
        $scope.selection = $scope.items[0];

        $scope.getEmissionState = function () {
            storageService.getEmissionState(function (emissionState) {
                $scope.emissionState = emissionState;
                $scope.$apply();
            });
        };

        $scope.getTimeResolution = function () {
            storageService.getTimeResolution(function (timeResolution) {
                $scope.options.timeResolution = timeResolution;
                $scope.$apply();
            });
        };

        $scope.getEmissionState();
        $scope.getTimeResolution();

        $scope.changeEmissionState = function () {
            storageService.getEmissionState(function (emissionState) {
                if (emissionState == 'START') {
                    storageService.setEmissionState('END');
                    $scope.emissionState = 'END';
                    $scope.$apply();
                } else {
                    storageService.setEmissionState('START');
                    $scope.emissionState = 'START';
                    $scope.$apply();
                }
            });
            chrome.runtime.sendMessage({"EmissionState": $scope.emissionState});
        };

        $scope.setTimeResolution = function () {
            storageService.setTimeResolution($scope.options.timeResolution);
            chrome.runtime.sendMessage({"TimeResolution": $scope.options.timeResolution});
        };

        $scope.login = function () {
            //only storage, todo check if in database
            storageService.setEmailAndPassword($scope.options.email, $scope.options.password);
            chrome.runtime.sendMessage({"Login": $scope.options.email, "Password": $scope.options.password});
        };

        $scope.redirect = function () {
            var newURL = "http://localhost:9000/users";
            chrome.tabs.create({url: newURL});
        };
    });
