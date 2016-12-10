import { Component } from "@angular/core";
import { User } from "../_common/user";
import { UserService } from "../_common/user.service";
@Component({
    selector: 'schedule-comp',
    templateUrl: 'schedule.template.html',
    styleUrls: ["./schedule.style.scss".toString()],
    providers: [UserService]
})

export class ScheduleComponent {
    userService = new UserService();
    countFields = [0, 0, 0, 0, 0, 0, 0];
    days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    constructor() {
    }

    addInput(day:number) {
        var fieldName1= "field-" + this.days[day] + this.countFields[day];
        this.countFields[day] += 1;
        var fieldName2= "field-" + this.days[day] + this.countFields[day];
        this.countFields[day] += 1;

        document.getElementById('fields-'+this.days[day]).innerHTML+=
            '<div id="'+fieldName1+'"><input type="time" name="'+fieldName1+'" size="5" /> - ' +
            '<input type="time" name="'+fieldName2+'" size="5"/></div> ';

    }

    remInput(day:number) {
        if(this.countFields[day] < 2)
            return;
        this.countFields[day] -= 2;
        var fieldName1= "field-" + this.days[day] + this.countFields[day];

        var el = document.getElementById(fieldName1);
        el.parentNode.removeChild(el);

    }

}
