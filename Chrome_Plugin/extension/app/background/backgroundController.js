angular.module('myApp', [])
    .controller('BackgroundController', function ($scope, serverService) {
        serverService.startService();

        chrome.runtime.onMessage.addListener(
            function (message) {
                console.log(message);
                serverService.processMessage(message);
            }
        );
    });
