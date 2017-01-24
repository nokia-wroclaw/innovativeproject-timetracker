import {Component} from "@angular/core";
import {TimelineSettings} from "../_common/timelineSettings";
import {FormsModule, NgForm} from "@angular/forms";
import "vis/dist/vis.css";
var vis = require("vis/dist/vis.js");
@Component({
    selector: 'index-comp',
    templateUrl: 'index.template.html',
    styleUrls: ["./index.style.scss".toString()]
})

export class IndexComponent {

    model = new TimelineSettings('Kruk07', "2016-12-12", "2017-01-16");
    timelineView = false;
    textView = true;
    submitted = false;
    isSubmitted = true;
    numberOfEditingDays = 3;
    listOfEditingDays = [''];
    actualDateEditing = '';
    actualDateNrEditing = 0;
    editingTables = [[['14:00', '15:00'],['17:00', '19:00'], ['19:15', '22:00']], [['9:00', '12:30'],['13:45', '16:00']]];
    constructor() {
    }

    onSubmit() {
        this.submitted = true;
    }

    changePartView(num: number) {
        if(num == 1) {
            this.timelineView = false;
            this.textView = true;
        } else {
            this.timelineView = true;
            this.textView = false;
        }
    }

    changeEditingDay(whichDay: number) {
        this.actualDateEditing = this.listOfEditingDays[whichDay];
    }

    changeEditingDayInput(whichDay: string) {
        var whichDayNr = 0;
        for(var i=0; i<this.listOfEditingDays.length; i++) {
            if(whichDay == this.listOfEditingDays[i]) {
                whichDayNr = i;
                break;
            }
        }
        this.actualDateNrEditing = whichDayNr;
    }

    submitDayChanges() {
        /*save changes to the table*/
        var inputFrom = "9:00";
        var inputTo = "10:00";
        for(var i=0; i<this.editingTables[this.actualDateNrEditing].length; i++) {
            //here checking values? (10:00 - 9:00)
            var inputFrom = (<HTMLInputElement>document.getElementById("edTables-"+i+"-1")).value;
            var inputTo = (<HTMLInputElement>document.getElementById("edTables-"+i+"-2")).value;

            /*compare if inputTo is later than inputFrom (as time)*/
            if(inputFrom.length != 0) {
                if(inputFrom.length == 4)
                    var inputFrom2 = "0" + inputFrom + ":00";
                else
                    var inputFrom2 = inputFrom + ":00";

                if(inputTo.length == 4)
                    var inputTo2 = "0" + inputTo + ":00";
                else
                    var inputTo2 = inputTo + ":00";

                if (inputFrom2 >= inputTo2) {
                    document.getElementById("serverAnswer").innerHTML = "Incorrect hours!";
                    return;
                }
            }
            this.editingTables[this.actualDateNrEditing][i] = ([inputFrom, inputTo]);
        }


        var xhttp = new XMLHttpRequest();

        var dateStr = this.actualDateEditing;

        //building sending msg
        var dateStamp = (new Date(this.actualDateEditing)).getTime();
        var sendingMsg = "{\"date\":" + dateStamp.toString() + ", \"periods\": [";
        for(var i=0; i<this.editingTables[this.actualDateNrEditing].length; i++) {
            if(this.editingTables[this.actualDateNrEditing][i][0].length == 0) {
                if(this.editingTables[this.actualDateNrEditing][i][1].length == 0) {
                    continue;
                }
                document.getElementById("serverAnswer").innerHTML = "Incorrect hours!";
                return;
            }
            sendingMsg += "{"
            sendingMsg += "\"begin\":"+ "\"" + this.editingTables[this.actualDateNrEditing][i][0] + "\"";
            sendingMsg += ",\"end\":"+ "\"" + this.editingTables[this.actualDateNrEditing][i][1] + "\"";
            sendingMsg += "}"

            if(i+1<this.editingTables[this.actualDateNrEditing].length &&
                this.editingTables[this.actualDateNrEditing][i+1][0].length != 0 ) {
                sendingMsg += ","
            }
        }

        sendingMsg += "]}";

        //document.getElementById("serverAnswer").innerHTML = sendingMsg;



        xhttp.open("POST", "/sendtimelinechanges", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                document.getElementById("serverAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send(sendingMsg);

    }

    addSpaceHours(upOrDown: number, nearWhichHours: number) {
        this.editingTables[this.actualDateNrEditing].splice(nearWhichHours + upOrDown, 0, new Array());
    }

    deleteHours(whichHours:number) {
        this.editingTables[this.actualDateNrEditing].splice(whichHours, 1);

        if(this.editingTables[this.actualDateNrEditing].length == 0) {
            this.editingTables[this.actualDateNrEditing].push(new Array());
        }
    }

    generateTextTimeline(response: string, date1: String, date2: string) {
        //this.numberOfEditingDays = (date2.getDate() - date1.getDate())/86400000 + 1;
        var d1 = new Date(date1);
        var d2 = new Date(date2);
        this.actualDateEditing = d1.getFullYear() + '-' + (d1.getMonth()+1) + '-' + d1.getDate();
        this.listOfEditingDays = [];
        while (d1.getTime() <= d2.getTime()) {
            this.listOfEditingDays.push(d1.toISOString().substr(0, 10));
            //this.listOfEditingDays.push(d1.getFullYear() + '-' + (d1.getMonth()+1) + '-' + d1.getDate());
            d1.setDate(d1.getDate() + 1);
        }
        this.numberOfEditingDays = this.listOfEditingDays.length - 1;

        this.editingTables = [[[]]];
        var jsonResponse = JSON.parse(response);
        var tmpArray =[['14:00', '15:00'], ['17:00', '18:00']];
        tmpArray.length = 0;

        var iteratorJson = 0;
        var iteratorList = 0;

        while(iteratorJson<jsonResponse.length && iteratorList<this.listOfEditingDays.length) {
            var tmp = jsonResponse[iteratorJson].begin.split("/");

            //7/12/2016@15:00 -> 2016-21-7
            d1 = new Date(tmp[2].split("@")[0] + "-" + tmp[1] + "-" + tmp[0]);
            /*if(d1.getFullYear() + '-' + (d1.getMonth()+1) + '-' + d1.getDate() == this.listOfEditingDays[iteratorList]) {*/
            if(d1.toISOString().substr(0, 10) == this.listOfEditingDays[iteratorList]) {
                tmpArray.push([jsonResponse[iteratorJson].begin.split("@")[1], jsonResponse[iteratorJson].end.split("@")[1]]);
                iteratorJson++;
            } else {
                if(tmpArray.length != 0) {
                    this.editingTables[iteratorList] = (tmpArray);
                    var tmpArray = [['14:00', '15:00'], ['17:00', '18:00']];
                    tmpArray.length = 0;
                } else {
                    this.editingTables[iteratorList] = [[]];
                    this.editingTables.push();
                }
                iteratorList++;
            }
        }

        //for last item
        if(tmpArray.length != 0) {
            this.editingTables[iteratorList] = (tmpArray);
            var tmpArray = [['14:00', '15:00'], ['17:00', '18:00']];
            tmpArray.length = 0;
        } else {
            this.editingTables[iteratorList] = [[]];
            this.editingTables.push();
        }

        iteratorList++;
        while(iteratorList<this.listOfEditingDays.length) {
            this.editingTables[iteratorList] = [[]];
            this.editingTables.push();
            iteratorList++;
        }

    }

    getTimeline(timelineForm: NgForm) {
        this.isSubmitted = false;
        document.getElementById('visualization').innerHTML = "";
        /*counting number of days*/

        var xhttp = new XMLHttpRequest();

        var fromDateStr = timelineForm.value.fromDate.split("-")[2] + "/" +
            timelineForm.value.fromDate.split("-")[1] + "/" +
            timelineForm.value.fromDate.split("-")[0] + "@00:00";

        var toDateStr = timelineForm.value.toDate.split("-")[2] + "/" +
            timelineForm.value.toDate.split("-")[1] + "/" +
            timelineForm.value.toDate.split("-")[0] + "@23:59";

        var params = JSON.stringify({
            begin: fromDateStr,
            end: toDateStr
        });


        xhttp.open("POST", "/userinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                //document.getElementById('serverAnswer').innerHTML = xhttp.responseText;
                var jsonResponse = JSON.parse(xhttp.responseText);
                var container = document.getElementById('visualization');
                var options = new Array();
                var data = new Array();

                //max days in one Row
                var maxDaysInRow = 7;

                //start-end date for a one row
                var dateStart = new Date();
                var dateEnd = new Date();

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
                if (jsonResponse.length >= 1) {

                    dateStart = new Date(reverseDate.split("/")[2] + '-'
                        + reverseDate.split("/")[1] + '-'
                        + reverseDate.split("/")[0]);
                    dateEnd = new Date();
                    dateEnd.setDate(dateStart.getDate() + (maxDaysInRow - 1));
                    options.push({min: dateStart.toDateString() + ' 00:00', max: dateEnd.toDateString() + ' 23:59'});
                    data.push(new vis.DataSet(options[j]));
                    j = j + 1;
                }
                /*for every received "work signal" */
                for (var i = 0; i < jsonResponse.length; i++) {

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
                    if (diffDays >= maxDaysInRow) {

                        reverseDate = jsonResponse[i].begin.split("@")[0];
                        dateStart = new Date(reverseDate.split("/")[2] + '-'
                            + reverseDate.split("/")[1] + '-'
                            + reverseDate.split("/")[0]);
                        dateEnd = new Date();
                        dateEnd.setDate(dateStart.getDate() + maxDaysInRow - 1);
                        options.push({
                            min: dateStart.toDateString() + ' 00:00',
                            max: dateEnd.toDateString() + ' 23:59'
                        });
                        data.push(new vis.DataSet(options[j]));
                        j = j + 1;

                    } else {

                        timeDiff = Math.abs(actualCheckingDateEnd.getTime() - dateStart.getTime());
                        diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                        /*if start of received "work signal" has a date that exceeds max days in one row - split it into 2 rows*/
                        if (diffDays >= maxDaysInRow) {

                            actualCheckingDateTmp = new Date(actualCheckingDateEnd.getMonth(),
                                actualCheckingDateEnd.getDay(),
                                actualCheckingDateEnd.getFullYear());

                            /*count how many days cant fit into actual row*/
                            while (diffDays >= maxDaysInRow) {
                                howManyDaysToCut += 1;
                                actualCheckingDateTmp = new Date(actualCheckingDateEnd.getMonth(),
                                    (actualCheckingDateEnd.getDay() - howManyDaysToCut),
                                    actualCheckingDateEnd.getFullYear());

                                timeDiff = Math.abs(actualCheckingDateTmp.getTime() - dateStart.getTime());
                                diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                            }
                            howManyDaysToCut = 0;

                            dateEnd = new Date(dateStart.getMonth(), dateStart.getDay() + 6, dateStart.getFullYear());
                            options.push({
                                min: actualCheckingDateBegin.toDateString() + ' 00:00',
                                max: actualCheckingDateTmp.toDateString() + ' 23:59'
                            });
                            j = j + 1;
                            data[j - 1].add([{
                                id: i, content: ' ',
                                start: actualCheckingDateBegin.toDateString() + ' ' + jsonResponse[i].begin.split("@")[1],
                                end: actualCheckingDateTmp.toDateString() + ' 23:59'
                            }]);

                            options.push({
                                min: actualCheckingDateTmp.toDateString() + ' 00:00',
                                max: actualCheckingDateEnd.toDateString() + ' 23:59'
                            });
                            j = j + 1;
                            data[j - 1].add([{
                                id: i, content: ' ',
                                start: actualCheckingDateTmp.toDateString() + ' 00:01',
                                end: actualCheckingDateEnd.toDateString() + ' ' + jsonResponse[i].end.split("@")[1]
                            }]);

                            dateStart = new Date(actualCheckingDateTmp.getMonth(),
                                actualCheckingDateTmp.getDay(),
                                actualCheckingDateTmp.getFullYear());

                        } else {

                            data[j - 1].add([{
                                id: i, content: ' ',
                                start: actualCheckingDateBegin.toDateString() + ' ' + jsonResponse[i].begin.split("@")[1],
                                end: actualCheckingDateEnd.toDateString() + ' ' + jsonResponse[i].end.split("@")[1]
                            }]);
                        }
                    }

                }
                /*display created rows*/
                for (var k = 0; k < j; k++) {
                    var timeline = new vis.Timeline(container, data[k], options[k]);
                }


                /*generate text form for editing in the end*/
                this.generateTextTimeline(xhttp.responseText, timelineForm.value.fromDate, timelineForm.value.toDate);
            }

        };
        xhttp.send(params);
    }

    generationModel = new TimelineSettings('Kruk07', "2017-01-03", "2017-01-16");

    getExcel(timelineForm: NgForm) {
        var xhttp = new XMLHttpRequest();

        var fromDate = new Date(timelineForm.value.fromDate);
        var toDate = new Date(timelineForm.value.toDate);

        var params = JSON.stringify({
            begin: fromDate.getTime(),
            end: toDate.getTime()
        });

        xhttp.open("POST", "/excel", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function () {
            if(xhttp.readyState === XMLHttpRequest.DONE && xhttp.status === 200) {
                console.log("Generation successful");

                var str = xhttp.responseText;
                var uri = 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8,' + str;
                //var uri = 'data:text/plain;charset=utf-8,' + str;
                var downloadLink = document.createElement("a");
                downloadLink.href = uri;
                downloadLink.download = "excel.xlsx";

                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);

            } else {
                console.log("Generation error");
            }
        };
        xhttp.send(params);
    }
}