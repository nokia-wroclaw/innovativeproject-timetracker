'use strict';

angular.module('index').service('serverService',
    function ($http, $interval, storageService) {

    this.repeatSend = function($repeatTime) {
        this.sending = $interval(this.send(), $repeatTime);
    };

    this.send = function() {
        $http({
            method: "POST",
            url: "http://localhost:9000/user",
            params: {surname: "user", name: "pwd", login: "bka", email: "bla", password: "asd"}
        }).then(function mySucces(response) {
            storageService.sendingStateText = 'END';
            storageService.sync();
        }, function myError(response) {
            storageService.sendingStateText = 'connection error';
        });
    };
    
    this.stop = function () {
        $interval.cancel(this.sending);
    }
});
