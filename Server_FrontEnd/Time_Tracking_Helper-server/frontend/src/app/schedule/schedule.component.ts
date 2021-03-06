import { Component } from "@angular/core";
import { User } from "../_common/user";
import { UserService } from "../_common/user.service";
import { NgForm } from "@angular/forms";

@Component({
    selector: 'schedule-comp',
    templateUrl: 'schedule.template.html',
    styleUrls: ["./schedule.style.scss".toString()],
    providers: [UserService]
})

export class ScheduleComponent {

    submitted = false;
    userService = new UserService();
    days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];

    /*Two-dimensional array that holds information which inputs are displayed */
    displayedInputs = [[false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false],
        [false, false, false, false, false, false, false, false]];

    constructor() {
        this.getSchedule();
    }

    onSubmit() {
        this.submitted = true;
    }

    getSchedule() {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "/fullschedule", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                // document.getElementById("schedule-info").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send();

    }
    sendSchedule(scheduleForm:NgForm) {
        var inputValue = (<HTMLInputElement>document.getElementById("field_monday00")).value;

        /*holds string that will be send to server as json*/
        var sendingStr = "{ \"schedule\": [";

        /*holds information if every input is correct*/
        var validTimeFormat = true;
        var i = 0; var j = 0;
        var i2 = 0;
        while(i < 7) {

            /*If day does have any inputs*/
            if(this.displayedInputs[i][0] == true) {

                while(this.displayedInputs[i][j] != false && j < 8 && validTimeFormat == true) {
                    sendingStr += "{\"day\": " + "\""+ this.days[i] +"\"" + ", ";
                    inputValue = (<HTMLInputElement>document.getElementById("field_"+this.days[i]+j+"0")).value;
                    /*disabled input*/
                    document.getElementById("field_"+this.days[i]+j+"0").setAttribute('disabled','disabled');
                    if(this.checkValue(inputValue) == false) {
                        validTimeFormat = false;
                    }
                    sendingStr += "\"begin\": " + "\""+ inputValue + "\"";

                    inputValue = (<HTMLInputElement>document.getElementById("field_"+this.days[i]+j+"1")).value;
                    /*disabled input*/
                    document.getElementById("field_"+this.days[i]+j+"1").setAttribute('disabled','disabled');
                    this.checkValue(inputValue);
                    sendingStr += ", \"end\": " + "\"" + inputValue + "\"" +  "}";

                    if(j+1 < 8 && this.displayedInputs[i][j+1] != false)
                        sendingStr += ", ";
                    j++;
                }
                i2 = i+1;
                while(i2 < 7) {
                    if(this.displayedInputs[i2][0] == true) {
                        sendingStr += ", ";
                        break;
                    }
                    i2++;
                }
            }

            j = 0;
            i++;
        }
        sendingStr += "]}";


        if(validTimeFormat == false) {
            document.getElementById("schedule-info").innerHTML = "Invalid time format!";
            return;
        }

        //UNCOMMENT WHEN SERVER READY

        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/setschedule", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                //document.getElementById("schedule-info").innerHTML = "Successful updated!" + inputValue;
                /*document.getElementById("schedule-info").innerHTML = sendingStr + " <br>" +  xhttp.responseText;*/
                document.getElementById("schedule-info").innerHTML = "Successful updated!"
            }
        }
        xhttp.send(sendingStr);



        //TODO: REMOVE
        //document.getElementById("schedule-info").innerHTML = sendingStr;
    }

    /*Checks if value is in HH:MM 24h format*/
    checkValue(val:String) {
        var regex = val.match(/^([01]?\d|2[0-3]):?([0-5]\d)/i);
        if (regex != null)
            return true;
        return false;
    }

    /*Displays next pair of inputs for specific day (max 8 pairs)*/
    addInput(day:number) {
        var i = 0;
        while(this.displayedInputs[day][i] != false && i < 8) {
            i++;
        }
        this.displayedInputs[day][i] = true;
        document.getElementById('fields-'+this.days[day]+i).style.display = "inline";
        document.getElementById('fields-'+this.days[day]+'-span'+i).style.display = "inline";
    }

    /*Hide one pair of inputs for specific day*/
    remInput(day:number,i:number) {
        while(i < 7 && this.displayedInputs[day][i] == true) {
            (<HTMLInputElement>document.getElementById("field_"+this.days[day]+i+"0")).value=(<HTMLInputElement>document.getElementById("field_"+this.days[day]+(i+1)+"0")).value;;
            (<HTMLInputElement>document.getElementById("field_"+this.days[day]+i+"1")).value=(<HTMLInputElement>document.getElementById("field_"+this.days[day]+(i+1)+"1")).value;;
            i++;
        }
        i--;
        this.displayedInputs[day][i] = false;
        document.getElementById('fields-'+this.days[day]+i).style.display = "none";
        document.getElementById('fields-'+this.days[day]+'-span'+i).style.display = "none";
    }

    switch(day:number,i:number) {
        document.getElementById("field_"+this.days[day]+i+"0").removeAttribute("disabled");
        document.getElementById("field_"+this.days[day]+i+"1").removeAttribute("disabled");
    }

}