import {Component} from "@angular/core";
import {FormsModule, NgForm} from "@angular/forms";

@Component({
    selector: 'settings-comp',
    templateUrl: 'settings.template.html',
    styleUrls: ["./settings.style.scss".toString()]
})

export class SettingsComponent {


    firstSettingsPartView = false;
    secondSettingsPartView = true;
    thirdSettingsPartView = true;
    constructor() {
    }

    userName = () => localStorage.getItem('authToken').split(" ")[1];
    userSurname = () => localStorage.getItem('authToken').split(" ")[2];

    changeEmail() {
        var emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        var newEmail = (<HTMLInputElement>document.getElementById("username")).value;
        if(emailRegex.test(newEmail) == false) {
            document.getElementById("serverAnswer").innerHTML = "Invalid e-mail!"
            return;
        }

        var xhttp = new XMLHttpRequest();

        //document.getElementById("serverAnswer").innerHTML = newEmail;
        var params = JSON.stringify({
            newemail: newEmail
        });

        xhttp.open("POST", "/changeuserinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                document.getElementById("serverAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send(params);

    }

    changeNameSurname() {

        var newName = (<HTMLInputElement>document.getElementById("name")).value;
        var newSurname = (<HTMLInputElement>document.getElementById("surname")).value;
        if (newName.length == 0) {
            document.getElementById("serverAnswer").innerHTML = "No name given!"
            return;
        } else if (newName.length > 32) {
            document.getElementById("serverAnswer").innerHTML = "New name is too long!"
            return;
        }

        if (newSurname.length == 0) {
            document.getElementById("serverAnswer").innerHTML = "No surname given!"
            return;
        } else if (newSurname.length > 64) {
            document.getElementById("serverAnswer").innerHTML = "New surname is too long!"
            return;
        }

        //document.getElementById("serverAnswer").innerHTML = newName + " " + newSurname;

        var xhttp = new XMLHttpRequest();

        var params = JSON.stringify({
            newname: newName,
            newsurname: newSurname
        });


        xhttp.open("POST", "/changeuserinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                document.getElementById("serverAnswer").innerHTML = xhttp.responseText;
                //Server gives new info for token?
            }
        };
        xhttp.send(params);

    }

    changePassword() {

        var oldPwd = (<HTMLInputElement>document.getElementById("oldPwd")).value;
        var newPwd1 = (<HTMLInputElement>document.getElementById("newPwd1")).value;
        var newPwd2 = (<HTMLInputElement>document.getElementById("newPwd2")).value;

        if(newPwd1 != newPwd2) {
            document.getElementById("serverAnswer").innerHTML = "Mismatch passwords!"
            return;
        }
        if(newPwd1.length < 6) {
            document.getElementById("serverAnswer").innerHTML = "Password must be at least 6 characters long!"
            return;
        }

        document.getElementById("serverAnswer").innerHTML = "OK";


        var xhttp = new XMLHttpRequest();


        var params = JSON.stringify({
            oldpwd: oldPwd,
            newpwd: newPwd1
        });

        xhttp.open("POST", "/changeuserinfo", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                document.getElementById("serverAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send(params);

    }

    changePartView(which:Number) {
        if(which == 1) {
            this.firstSettingsPartView = false;
            this.secondSettingsPartView = true;
            this.thirdSettingsPartView = true;
        } else  if(which == 2) {
            this.firstSettingsPartView = true;
            this.secondSettingsPartView = false;
            this.thirdSettingsPartView = true;
        } else  {
            this.firstSettingsPartView = true;
            this.secondSettingsPartView = true;
            this.thirdSettingsPartView = false;
        }
    }


}
