'use strict';

angular.module('index').service('serverService', function ($http) {

    this.sending = null;
    this.repeatSend = function($repeatTime) {
        this.sending = setInterval(this.send, $repeatTime);
    };

    this.send = function() {
        console.log("asd");
        $http({
            method: "POST",
            url: "http://localhost:9000/user",
            params: {surname: "user", name: "pwd", login: "bka", email: "bla", password: "asd"}
        }).then(function mySucces(response) {
        }, function myError(response) {
            console.log("connection error");
        });
    };

    this.stop = function () {
        //Interval.cancel(sending);
        console.log('siema', this.sending);
        console.log(clearInterval(this.sending));
    }
});
