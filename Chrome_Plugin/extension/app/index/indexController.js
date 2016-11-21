'use strict';

angular.module('myApp', [])
    .controller('EmmitStateController', function ($scope, storageService) {

        $scope.options = {
            timeResolution: "",
            email: "",
            password: ""
        };

        $scope.getEmissionState = function () {
            storageService.getEmissionState(function (emissionState) {
                $scope.emissionState = emissionState;
                $scope.$apply();
            });
        };

        $scope.getTimeInterval = function () {
            storageService.getTimeResolution(function (timeResolution) {
                $scope.options.timeResolution = timeResolution;
                $scope.$apply();
            });
        };

        $scope.getEmissionState();
        $scope.getTimeInterval();

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
        };

        $scope.syncTimeResolution = function () {
            storageService.setTimeRes($scope.options.timeResolution);
        };

        $scope.login = function () {
            //only storage, todo check if in database
            storageService.setEmailAndPassword($scope.options.email, $scope.options.password);
        };

        $scope.redirect = function () {
            var newURL = "http://localhost:9000/users";
            chrome.tabs.create({url: newURL});
        };

        $scope.items = ['Home', 'Options'];
        $scope.selection = $scope.items[0];
    });
