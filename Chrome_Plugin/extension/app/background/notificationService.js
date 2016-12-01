
var myNotificationID = null;
angular.module('myApp').service('notificationService', function ($http) {

    this.startService = function () {
        createNotification();
    };

    var createNotification = function() {
        chrome.notifications.create(
            'isWorking',{
                type: 'basic',
                iconUrl: '../icon.png',
                title: "Time tracking question",
                message: "Are you working now?",
                requireInteraction: true,
                buttons: [
                    { title: 'Yes' },
                    { title: 'No' }
                ]
            },
            function(id) {
                myNotificationID = id;
            }
        );
    };

    chrome.notifications.onButtonClicked.addListener(function(notificationId, buttonIndex) {
        if (notificationId == myNotificationID) {
            if (buttonIndex == 0) {
                alert("You are working!");
            } else {
                alert("You are not working!");
            }
            chrome.notifications.clear(myNotificationID);
        }
    });
});