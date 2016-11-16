angular.module('myApp', [])
    .controller('BackgroundController', function ($scope, serverService) {
        serverService.startService();
    });
