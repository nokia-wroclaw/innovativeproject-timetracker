import { Component } from "@angular/core";
import { User } from "../_common/user";
import { PrivilegesAddReqSettings } from "../_common/privilegesAddReqSettings";
import { UserService } from "../_common/user.service";
import { NgForm } from "@angular/forms";

@Component({
    selector: 'privileges-comp',
    templateUrl: 'privileges.template.html',
    styleUrls: ["./privileges.style.scss".toString()],
    providers: [UserService]
})

export class PrivilegesComponent {

    model = new PrivilegesAddReqSettings('');
    submitted = false;
    userService = new UserService();
    firstPrivilegesPartView = false;
    secondPrivilegesPartView = true;

    invitationsTable = [["Wojciech","Ciech", "wojt123"]];

    accInvitationsTable = [["Grzegorz","Brzęczyszczykiewicz", "Brzesio", null], ["Jacek","Papuga", "Kecaj", 60], ["Pan","Pies", "Doge", 21]];

    othersInvitationsTable = [["Henryk","Niesienkiewicz", "Brzeczek1"], ["Jan","Mroźny", "Niegrozny"]];

    othersAccInvitationsTable= [["Julian","Mydełko", "Wydło"], ["Bartłomiej","Frukt", "Owoc"], ["Stefan","Fasten", "Klasa"], ["Józef","Promil", "Nono"]];

    constructor() {
        this.getPrivRequests();
        this.getPrivList();
        this.getOthersPrivRequests();
        this.getOthersPrivList();
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
    getPrivRequests() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "requestyou"
        });
        xhttp.open("POST", "/getprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                this.invitationsTable = [[]];
                this.invitationsTable.pop();
                var jsonResponse = JSON.parse(xhttp.responseText);
                for(let xResponse of jsonResponse){
                    //this.invitationsTable.push([xResponse.name, xResponse.surname, xResponse.nick]);
                    this.invitationsTable.push(["Unknown Name", "Unknown Surname", xResponse.userto]);
                }
               // document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);
    }

    getOthersPrivRequests() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "requestother"
        });
        xhttp.open("POST", "/getprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                this.othersInvitationsTable = [[]];
                this.othersInvitationsTable.pop();
                var jsonResponse = JSON.parse(xhttp.responseText);
                for(let xResponse of jsonResponse){
                    //this.invitationsTable.push([xResponse.name, xResponse.surname, xResponse.nick]);
                    this.othersInvitationsTable.push(["Unknown Name", "Unknown Surname", xResponse.userfrom]);
                }
                // document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);
    }

    getPrivList() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "privilegesyou"
        });
        xhttp.open("POST", "/getprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                this.accInvitationsTable = [[]];
                this.accInvitationsTable.pop();
                var jsonResponse = JSON.parse(xhttp.responseText);
                for(let xResponse of jsonResponse){
                    //this.invitationsTable.push([xResponse.name, xResponse.surname, xResponse.nick]);
                    this.accInvitationsTable.push(["Unknown Name", "Unknown Surname", xResponse.userto]);
                }
                // document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);
    }

    getOthersPrivList() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "privilegesother"
        });
        xhttp.open("POST", "/getprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                this.othersAccInvitationsTable = [[]];
                this.othersAccInvitationsTable.pop();
                var jsonResponse = JSON.parse(xhttp.responseText);
                for(let xResponse of jsonResponse){
                    //this.invitationsTable.push([xResponse.name, xResponse.surname, xResponse.nick]);
                    this.othersAccInvitationsTable.push(["Unknown Name", "Unknown Surname", xResponse.from]);
                }
                // document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);
    }

    sendPrivReq(type: String, table: String, nick:String) {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: type,
            login: nick
        });
        xhttp.open("POST", "/sendprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        document.getElementById("test").innerHTML = params;
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                if(table === "privReq")
                    this.getPrivRequests();
                else if(table === "privList")
                    this.getPrivList();
                else if(table === "othersPrivReq") {
                    this.getOthersPrivRequests();
                    this.getOthersPrivList();
                } else if(table === "othersPrivList")
                    this.getOthersPrivList();
                document.getElementById("test").innerHTML = xhttp.responseText;
            }
        };

        xhttp.send(params);

    }

    addPrivRequest(privilegesAddForm:NgForm) {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            type: "add",
            login: privilegesAddForm.value.login
        });
        xhttp.open("POST", "/sendprivelegesandrequests", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                if(xhttp.responseText == "ADDED") {
                    this.getPrivRequests();
                } else if(xhttp.responseText === "REQUEST ALREADY IN DATABASE") {
                    //TODO: HERE INFO FOR USER
                }
            }
        };
        xhttp.send(params);
    }

    onSubmit() {
        this.submitted = true;
    }

}
