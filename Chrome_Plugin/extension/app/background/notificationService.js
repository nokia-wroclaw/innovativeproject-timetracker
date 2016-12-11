var myNotificationID = null;
var port = null;
var schedule = [];
var shouldWorkTitle = "You should work now";
var shouldNotWorkTitle = "You shouldn't work now";
var shouldWork = false;
var isLogged = false;
var isTracking = false;
var reminderInterval = 0;
var nextNotificationStartTime = 0;
var nextNotificationTimerID = 0;
angular.module('myApp')
    .service('notificationService', function ($http, trackingService, storageService) {
        var _this = this;

        this.setPort = function (newPort) {
            port = newPort;
            port.onMessage.addListener(function (message) {
                var keys = Object.keys(message);
                if (keys.includes("reminder")) {
                    storageService.updateStorage({
                        reminder: message.reminder
                    });
                    changeReminderTime(message.reminder);
                }
                if (keys.includes("isTracking")) {
                    isTracking = message.isTracking;
                    resetReminder();
                }
                if (keys.includes("login")) {
                    isLogged = message.isLogged;
                    resetReminder();
                }
            });
        };

        var resetReminder = function () {
            clearInterval(nextNotificationTimerID);
            if (reminderInterval && shouldWork != isTracking && isLogged)
                remindNotification(reminderInterval);
        };

        var changeReminderTime = function (reminder) {
            if (shouldWork != isTracking && isLogged) {
                clearInterval(nextNotificationTimerID);
                var nextNotificationNewEndTime = nextNotificationStartTime + reminder * 60 * 1000;
                //var nextNotificationNewEndTime = nextNotificationStartTime + reminder * 1000; //for tests
                var tempInterval = reminder * 60 * 1000;
                //var tempInterval = reminder * 1000; //for tests
                if (nextNotificationNewEndTime > Date.now())
                    tempInterval = tempInterval - (Date.now() - nextNotificationStartTime);
                remindNotification(tempInterval);
            }
            //reminderInterval = reminder * 1000; //for tests
            reminderInterval = reminder * 60 * 1000;
        };

        this.startService = function () {
            loadStorage();
            fetchSchedule();
            setTimeout(checkSchedule, 200);
        };

        var loadStorage = function () {
            storageService.getStorage(function (keys) {
                isTracking = keys.isTracking;
                isLogged = keys.isLogged;
                //reminderInterval = keys.reminder * 1000; //for tests
                reminderInterval = keys.reminder * 60 * 1000;
            })
        };

        var checkSchedule = function () {
            var index = 0;
            var currentDate = new Date();
            for (index; index < schedule.length; ++index) {
                if (currentDate < schedule[index].date) {
                    shouldWork = (schedule[index].status == 'end');
                    setTimeout(checkSchedule, schedule[index].date.getTime() - currentDate.getTime());
                    if (shouldWork != isTracking && isLogged)
                        createNotification();
                    break;
                }
            }
            if (index == schedule.length && index != 0) {
                shouldWork = (schedule[index - 1].status == 'start');
                var midnight = new Date(Date.UTC(currentDate.getFullYear(), currentDate.getMonth(),
                        currentDate.getDate() + 1) + currentDate.getTimezoneOffset() * 60 * 1000);
                setTimeout(_this.startService, midnight.getTime() - currentDate.getTime());
                if (shouldWork != isTracking && isLogged)
                    createNotification();
            }
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
                'isTracking', {
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
            if (reminderInterval)
                remindNotification(reminderInterval);
        };

        var remindNotification = function (timeInterval) {
            nextNotificationStartTime = Date.now();
            nextNotificationTimerID = setTimeout(function () {
                if (shouldWork != isTracking && isLogged) {
                    createNotification();
                }
            }, timeInterval);
        };

        chrome.notifications.onButtonClicked.addListener(function (notificationId, buttonIndex) {
            if (notificationId == myNotificationID) {
                if (isLogged) {
                    var yesClicked = (buttonIndex == 0);
                    if (yesClicked != isTracking) {
                        try {
                            port.postMessage({isTracking: yesClicked});
                        } catch (err) {
                            console.log("not connected");
                        }
                        isTracking = yesClicked;
                        clearInterval(nextNotificationTimerID);
                        trackingService.changeTrackingState(yesClicked);
                    }
                }
                chrome.notifications.clear(myNotificationID);
            }
        });
    });