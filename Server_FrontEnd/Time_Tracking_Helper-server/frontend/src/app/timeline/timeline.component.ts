import { Component } from "@angular/core";
import { TimelineSettings } from "../_common/timelineSettings.ts";
import {FormsModule, NgForm} from "@angular/forms";
import "vis/dist/vis.css";
var vis = require("vis/dist/vis.js");
@Component({
    selector: 'timeline-comp',
    templateUrl: 'timeline.template.html',
    styleUrls: ["./timeline.style.scss".toString()]
})

export class TimelineComponent {

    model = new TimelineSettings('Kruk07', "2016-12-12", "2016-12-13");
    range = 7; // number of days to get
    usersToWatch = [["Henryk","Niesienkiewicz", "Kruk07"], ["Jan","Mroźny", "Niegrozny"]];
    constructor() {
        this.getUsersToWatch();
        var fromDefault = new Date();
        this.model = new TimelineSettings('Kruk07', (new Date((new Date()).getTime()-600000000).toISOString().substr(0, 10)), (new Date().toISOString().substr(0, 10)));
    }

    changeRange(num: number) {
        this.range = num;
    }

    getUsersToWatch() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "privilegesyou"
        });

        xhttp.open("POST", "/getprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                this.usersToWatch = [[]];
                this.usersToWatch.pop();
                var jsonResponse = JSON.parse(xhttp.responseText);
                for(let xResponse of jsonResponse){
                    this.usersToWatch.push([xResponse.name, xResponse.surname, xResponse.userto]);
                }
                // document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);
    }

    remTimeline(numberOfUser: number) {
        document.getElementById('user-timeline-'+numberOfUser).innerHTML = "";
    }

    getTimeline(username: String, numberOfUser: number, timelineForm:NgForm) {
        var xhttp = new XMLHttpRequest();

        var params = "";
        if(this.range != 0) {
            var rng = this.range.toString();
            params = JSON.stringify({
                login: username,
                range: rng
            });
        } else {
            var fromDateStr = timelineForm.value.fromDate.split("-")[2] + "/" +
                timelineForm.value.fromDate.split("-")[1] + "/" +
                timelineForm.value.fromDate.split("-")[0] + "@00:00";

            var toDateStr = timelineForm.value.toDate.split("-")[2] + "/" +
                timelineForm.value.toDate.split("-")[1] + "/" +
                timelineForm.value.toDate.split("-")[0] + "@23:59";

            params = JSON.stringify({
                login: username,
                begin: fromDateStr,
                end: toDateStr
            });
        }
        //document.getElementById("serverAnswer2").innerHTML = params;

        xhttp.open("POST", "/otherinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function(){
            if (this.readyState == 4 && this.status == 200) {
                //document.getElementById("serverAnswer2").innerHTML = xhttp.responseText;
                var jsonResponse = JSON.parse(xhttp.responseText);
                var container = document.getElementById('user-timeline-'+numberOfUser);
                var options = new Array();
                var data = new Array();

                //GATE FOR USER CHOICE IN DISPLAYING
                var maxDaysInRow = 7;

                //start-end date for a one row
                var dateStart = new Date();;
                var dateEnd = new Date();;

                //How many rows prepared already
                var j = 0;

                //for changing 13/12/2016 to 2016-12-13
                var reverseDate = '';

                var timeDiff = 0;
                var diffDays = 0;

                var actualCheckingDateBegin = new Date();
                var actualCheckingDateEnd = new Date();

                //for cutting data when exceeds dateEnd
                var actualCheckingDateTmp = new Date();
                var howManyDaysToCut = 0;

                /*need to create first dateStart and dateEnd*/
                reverseDate = jsonResponse[0].begin.split("@")[0];
                if(jsonResponse.length >= 1) {

                    dateStart = new Date(reverseDate.split("/")[2] + '-'
                        + reverseDate.split("/")[1] + '-'
                        + reverseDate.split("/")[0]);
                    dateEnd = new Date();
                    dateEnd.setDate(dateStart.getDate() + (maxDaysInRow-1));
                    options.push({min: dateStart.toDateString() + ' 00:00', max: dateEnd.toDateString() + ' 23:59'});
                    data.push(new vis.DataSet(options[j]));
                    j = j+1;
                }
                /*for every received "work signal" */
                for(var i=0; i<jsonResponse.length; i++) {

                    reverseDate = jsonResponse[i].begin.split("@")[0];
                    actualCheckingDateBegin = new Date(reverseDate.split("/")[2] + '-'
                        + reverseDate.split("/")[1] + '-'
                        + reverseDate.split("/")[0]);
                    reverseDate = jsonResponse[i].end.split("@")[0];
                    actualCheckingDateEnd = new Date(reverseDate.split("/")[2] + '-'
                        + reverseDate.split("/")[1] + '-'
                        + reverseDate.split("/")[0]);

                    timeDiff = Math.abs(actualCheckingDateBegin.getTime() - dateStart.getTime());
                    diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                    /*if start of received "work signal" has a date that exceeds max days in one row - create new row*/
                    if(diffDays >= maxDaysInRow) {

                        reverseDate = jsonResponse[i].begin.split("@")[0];
                        dateStart = new Date(reverseDate.split("/")[2] + '-'
                            + reverseDate.split("/")[1] + '-'
                            + reverseDate.split("/")[0]);
                        dateEnd = new Date();
                        dateEnd.setDate(dateStart.getDate() + maxDaysInRow-1);
                        options.push({min: dateStart.toDateString() + ' 00:00', max: dateEnd.toDateString() + ' 23:59'});
                        data.push(new vis.DataSet(options[j]));
                        j = j+1;

                    } else {

                        timeDiff = Math.abs(actualCheckingDateEnd.getTime() - dateStart.getTime());
                        diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                        /*if start of received "work signal" has a date that exceeds max days in one row - split it into 2 rows*/
                        if(diffDays >= maxDaysInRow) {

                            actualCheckingDateTmp = new Date(actualCheckingDateEnd.getMonth(),
                                actualCheckingDateEnd.getDay(),
                                actualCheckingDateEnd.getFullYear());

                            /*count how many days cant fit into actual row*/
                            while(diffDays >= maxDaysInRow) {
                                howManyDaysToCut += 1;
                                actualCheckingDateTmp = new Date(   actualCheckingDateEnd.getMonth(),
                                    (actualCheckingDateEnd.getDay() - howManyDaysToCut),
                                    actualCheckingDateEnd.getFullYear());

                                timeDiff = Math.abs(actualCheckingDateTmp.getTime() - dateStart.getTime());
                                diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                            }
                            howManyDaysToCut = 0;

                            dateEnd = new Date(dateStart.getMonth(), dateStart.getDay()+6, dateStart.getFullYear());
                            options.push({min: actualCheckingDateBegin.toDateString() + ' 00:00',
                                max: actualCheckingDateTmp.toDateString() + ' 23:59'});
                            j = j+1;
                            data[j-1].add([{id: i, content: ' ',
                                start: actualCheckingDateBegin.toDateString() + ' ' + jsonResponse[i].begin.split("@")[1],
                                end: actualCheckingDateTmp.toDateString() + ' 23:59'}]);

                            options.push({min: actualCheckingDateTmp.toDateString() + ' 00:00',
                                max: actualCheckingDateEnd.toDateString() + ' 23:59'});
                            j = j+1;
                            data[j-1].add([{id: i, content: ' ',
                                start: actualCheckingDateTmp.toDateString() + ' 00:01',
                                end: actualCheckingDateEnd.toDateString() + ' ' + jsonResponse[i].end.split("@")[1]}]);

                            dateStart = new Date(actualCheckingDateTmp.getMonth(),
                                actualCheckingDateTmp.getDay(),
                                actualCheckingDateTmp.getFullYear());

                        } else {

                            data[j-1].add([{id: i, content: ' ',
                                start: actualCheckingDateBegin.toDateString() + ' ' + jsonResponse[i].begin.split("@")[1],
                                end: actualCheckingDateEnd.toDateString() + ' ' + jsonResponse[i].end.split("@")[1]}]);
                        }
                    }

                }
                /*display created rows*/
                for(var k = 0; k < j; k++) {
                    var timeline = new vis.Timeline(container, data[k], options[k]);
                }

            }

        };
        xhttp.send(params);
    }
}