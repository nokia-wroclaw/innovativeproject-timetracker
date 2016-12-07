var myNotificationID = null;
var port = null;
var emissionState = "";
var shouldWorkTitle = "You should work now";
var shouldNotWorkTitle = "You shouldn't work now";
var shouldWork = false;
var schedule = [];
var isLogged = false;
angular.module('myApp')
    .service('notificationService', function ($http, trackingService, storageService) {
        var _this = this;

        this.setPort = function (newPort) {
            port = newPort;
        };

        this.startService = function () {
            fetchSchedule();
            checkSchedule();
        };

        var checkSchedule = function () {
            updateUserState();
            setTimeout(function () {
                var index = 0;
                var currentDate = new Date();
                var isWorking = (emissionState == "END");
                for (index; index < schedule.length; ++index) {
                    if (currentDate < schedule[index].date) {
                        shouldWork = (schedule[index].status == 'end');
                        if (shouldWork != isWorking && isLogged)
                            createNotification();
                        setTimeout(checkSchedule, schedule[index].date.getTime() - currentDate.getTime());
                        break;
                    }
                }
                if (index == schedule.length && index != 0) {
                    shouldWork = (schedule[index - 1].status == 'start');
                    if (shouldWork != isWorking && isLogged)
                        createNotification();
                    var midnight = new Date(Date.UTC(currentDate.getFullYear(), currentDate.getMonth(),
                            currentDate.getDate() + 1) + currentDate.getTimezoneOffset() * 60 * 1000);
                    setTimeout(_this.startService, midnight.getTime() - currentDate.getTime());
                }
            }, 500);
        };

        var updateUserState = function () {
            storageService.getStorage(function (keys) {
                emissionState = keys.emissionState;
                isLogged = keys.isLogged;
            })
        };

        var fetchSchedule = function () {
            //get schedule for one day
            /*$http({
             method: "GET",
             url: "http://localhost:9000/schedule"
             }
             ).then(function (response) {
             console.log("schedule success", response);
             }, function (response) {
             console.error("schedule error", response);
             });*/
            //sample data
            var currentDate = new Date();
            var start1 = new Date(currentDate);
            start1.setMinutes(currentDate.getMinutes() - 1);
            var end1 = new Date(currentDate);
            end1.setMinutes(currentDate.getMinutes() + 1);
            var start2 = new Date(currentDate);
            start2.setMinutes(currentDate.getMinutes() + 2);
            var end2 = new Date(currentDate);
            end2.setMinutes(currentDate.getMinutes() + 3);
            schedule = [{
                date: start1,
                status: "start"
            }, {
                date: end1,
                status: "end"
            }, {
                date: start2,
                status: "start"
            }, {
                date: end2,
                status: "end"
            }]
        };

        var createNotification = function () {
            chrome.notifications.create(
                'isWorking', {
                    type: 'basic',
                    iconUrl: '../icon.png',
                    title: (shouldWork) ? shouldWorkTitle : shouldNotWorkTitle,
                    message: "Are you working?",
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
                updateUserState();
                setTimeout(function () {
                    if (isLogged) {
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
                    }
                    chrome.notifications.clear(myNotificationID);
                }, 200);
            }
        });
    });