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
    submitted = false;

    constructor() {

    }

    onSubmit() {
        this.submitted = true;
    }

    getTimeline(timelineForm: NgForm) {
        var xhttp = new XMLHttpRequest();

        //TODO: add date to params when server ready
        var params = JSON.stringify({
            login: timelineForm.value.login
        });

        xhttp.open("POST", "/userinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function(){
            if (this.readyState == 4 && this.status == 200) {
                var jsonResponse = JSON.parse(xhttp.responseText);
                var container = document.getElementById('visualization');
                var options = new Array();
                var data = new Array();

                var lastDate = '';
                var lastDate2 = '';
                var j = 0;
                var reverseDate = '';

                for(var i=0; i<jsonResponse.length; i++) {
                    /*If new day, then create new pair of options and data*/
                    if(jsonResponse[i].begin.split("@")[0] != reverseDate) {

                        /*13/12/2016 to 2016-12-13*/
                        reverseDate = jsonResponse[i].begin.split("@")[0];
                        lastDate = reverseDate.split("/")[2] + "-" + reverseDate.split("/")[1] + "-" + reverseDate.split("/")[0];

                        options.push({min: lastDate + ' 00:01', max: lastDate + ' 23:59'});
                        data.push(new vis.DataSet(options[j]));
                        j = j+1;
                    }
                    /*If end of work has a different date than start of work, split it into days*/
                    //Works only when less than 3 days length
                    if(jsonResponse[i].end.split("@")[0] != reverseDate) {

                        reverseDate = jsonResponse[i].end.split("@")[0];
                        lastDate2 = reverseDate.split("/")[2] + "-" + reverseDate.split("/")[1] + "-" + reverseDate.split("/")[0];
                        data[j-1].add([{id: i, content: ' ', start: lastDate + ' ' + jsonResponse[i].begin.split("@")[1], end: lastDate + '23:59'}]);

                        options.push({min: lastDate2 + ' 00:01', max: lastDate2 + ' 23:59'});
                        j = j+1;
                        data[j-1].add([{id: i, content: ' ', start: lastDate2 + ' 00:00', end: lastDate2 + ' ' + jsonResponse[i].end.split("@")[1]}]);

                    } else {
                        data[j-1].add([{id: i, content: ' ', start: lastDate + ' ' + jsonResponse[i].begin.split("@")[1], end: lastDate + ' ' + jsonResponse[i].end.split("@")[1]}]);
                    }

                }
                for(var k = 0; k < j; k++) {
                    var timeline = new vis.Timeline(container, data[k], options[k]);
                }

                //For testing
                //document.getElementById("serverAnswer").innerHTML = xhttp.responseText.toString();
            }
        };
        xhttp.send(params);
    }
}