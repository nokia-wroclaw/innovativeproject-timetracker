angular.module('myApp', [])
    .controller('BackgroundController', function (trackingService, notificationService) {

        trackingService.startService();
        notificationService.startService();

        chrome.extension.onConnect.addListener(function(port) {
            trackingService.setPort(port);
            notificationService.setPort(port);
        });
    });
