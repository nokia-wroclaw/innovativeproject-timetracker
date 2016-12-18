var myNotificationID = null;
var port = null;
var schedule = [];
var shouldWorkTitle = "You should work now";
var shouldNotWorkTitle = "You shouldn't work now";
var shouldWork = false;
var isLogged = false;
var isTracking = false;
var login = "";
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
                    login = message.login;
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
                var tempInterval = reminder * 60 * 1000;
                if (nextNotificationNewEndTime > Date.now())
                    tempInterval = tempInterval - (Date.now() - nextNotificationStartTime);
                remindNotification(tempInterval);
            }
            reminderInterval = reminder * 60 * 1000;
        };

        this.startService = function () {
            loadStorage();
            setTimeout(fetchSchedule, 200);
            setTimeout(checkSchedule, 400);
        };

        var loadStorage = function () {
            storageService.getStorage(function (keys) {
                isTracking = keys.isTracking;
                isLogged = keys.isLogged;
                login = keys.login;
                reminderInterval = keys.reminder * 60 * 1000;
            })
        };

        var checkSchedule = function () {
            var index = 0;
            var currentTime = Date.now();
            for (index; index < schedule.length; ++index) {
                if (currentTime < schedule[index].date) {
                    shouldWork = (schedule[index].status == 'end');
                    setTimeout(checkSchedule, schedule[index].date - currentTime);
                    if (shouldWork != isTracking && isLogged)
                        createNotification();
                    break;
                }
            }
            if (index == schedule.length && index != 0) {
                shouldWork = (schedule[index - 1].status == 'start');
                var currentDate = new Date();
                var midnight = new Date(Date.UTC(currentDate.getFullYear(), currentDate.getMonth(),
                        currentDate.getDate() + 1) + currentDate.getTimezoneOffset() * 60 * 1000);
                setTimeout(_this.startService, midnight.getTime() - currentDate.getTime());
                if (shouldWork != isTracking && isLogged)
                    createNotification();
            }
        };

        var fetchSchedule = function () {
            var currentDate = new Date();
            $http({
                    method: "POST",
                    url: "http://localhost:9000/getschedule",
                    data: {
                        login: login,
                        day: ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'][currentDate.getDay()]
                    }
                }
            ).then(function (response) {
                currentDate.setSeconds(0);
                response.data.forEach(function (period) {
                    if (period.begin) {
                        currentDate.setHours(parseInt(period.begin.substring(0, 2)));
                        currentDate.setMinutes(parseInt(period.begin.substring(3, 5)));
                        schedule.push({
                            date: currentDate.getTime(),
                            status: "start"
                        });
                    }
                    currentDate.setHours(parseInt(period.end.substring(0, 2)));
                    currentDate.setMinutes(parseInt(period.end.substring(3, 5)));
                    schedule.push({
                        date: currentDate.getTime(),
                        status: "end"
                    });
                });
            }, function (response) {
                console.error("schedule error", response);
            });
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