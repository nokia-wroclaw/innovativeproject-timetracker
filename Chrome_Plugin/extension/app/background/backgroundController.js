angular.module('myApp', [])
    .controller('BackgroundController', function (trackingService, notificationService) {
        trackingService.startService();
        notificationService.startService();

        chrome.runtime.onMessage.addListener(
            function (message) {
                console.log(message);
                trackingService.processMessage(message);
            }
        );
    });
