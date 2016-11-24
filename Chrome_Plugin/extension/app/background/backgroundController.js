angular.module('myApp', [])
    .controller('BackgroundController', function ($scope, serverService) {
        serverService.startService();

        chrome.runtime.onMessage.addListener(
            function(message) {
                console.log("background got a message");
                console.log(message);
                serverService.processMessage(message);
            }
        );
    });
