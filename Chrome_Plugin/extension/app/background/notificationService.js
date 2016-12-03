var myNotificationID = null;
var port = null;
var emissionState = "";
angular.module('myApp').service('notificationService', function ($http, trackingService, storageService) {

    this.setPort = function (newPort) {
        port = newPort;
    };

    this.startService = function () {
        //getStorage();
        createNotification();
    };

    /*var getStorage = function () {
        storageService.getStorage(function (keys) {
            emissionState = keys.emissionState;
        })
    };

    var getSchedule = function () {
        $http({
                method: "GET",
                url: "http://localhost:9000/timetable"
            }
        ).then(function (response) {
            console.log("schedule success", response);
        }, function (response) {
            console.error("schedule error", response);
        });
    };*/

    var createNotification = function () {
        chrome.notifications.create(
            'isWorking', {
                type: 'basic',
                iconUrl: '../icon.png',
                title: "Time tracking question",
                message: "Are you working now?",
                requireInteraction: true,
                buttons: [
                    {title: 'Yes'},
                    {title: 'No'}
                ]
            },
            function (id) {
                myNotificationID = id;
            }
        );
    };

    chrome.notifications.onButtonClicked.addListener(function (notificationId, buttonIndex) {
        if (notificationId == myNotificationID) {
            var state;
            if (buttonIndex == 0) {
                state = 'END';
            } else {
                state = 'START';
            }
            try {
                port.postMessage({'emissionState': state});
            } catch (err) {
                console.log("not connected");
            }
            trackingService.changeEmissionState(state);
            chrome.notifications.clear(myNotificationID);
        }
    });
});