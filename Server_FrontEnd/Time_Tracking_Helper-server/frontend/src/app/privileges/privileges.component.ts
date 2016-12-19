import { Component } from "@angular/core";
import { User } from "../_common/user";
import { UserService } from "../_common/user.service";
import { NgForm } from "@angular/forms";

@Component({
    selector: 'privileges-comp',
    templateUrl: 'privileges.template.html',
    styleUrls: ["./privileges.style.scss".toString()],
    providers: [UserService]
})

export class PrivilegesComponent {

    submitted = false;
    userService = new UserService();
    firstPrivilegesPartView = false;
    secondPrivilegesPartView = true;

    invitationsTable = [["Wojciech","Ciech"]];

    accInvitationsTable = [["Grzegorz","Brzęczyszczykiewicz", null], ["Jacek","Papuga", 60], ["Pan","Pies", 21]];

    othersInvitationsTable = [["Henryk","Niesienkiewicz"], ["Jan","Mroźny"]];

    othersAccInvitationsTable= [["Julian","Mydełko"], ["Bartłomiej","Frukt"], ["Stefan","Fasten"], ["Józef","Promil"]];

    constructor() {
    }

    //TODO: Sending changes to server without submit button?
    changePartView(which:Number) {
        if(which == 1) {
            this.firstPrivilegesPartView = false;
            this.secondPrivilegesPartView = true;
        } else {
            this.firstPrivilegesPartView = true;
            this.secondPrivilegesPartView = false;
        }
    }

    onSubmit() {
        this.submitted = true;
    }

}
