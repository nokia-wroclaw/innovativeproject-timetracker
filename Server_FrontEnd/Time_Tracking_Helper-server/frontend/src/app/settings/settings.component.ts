import {Component} from "@angular/core";
import {FormsModule, NgForm} from "@angular/forms";

@Component({
    selector: 'settings-comp',
    templateUrl: 'settings.template.html',
    styleUrls: ["./settings.style.scss".toString()]
})

export class SettingsComponent {

    constructor() {
    }

    userNick = () => localStorage.getItem('authToken').split(" ")[0];
    userName = () => localStorage.getItem('authToken').split(" ")[1];
    userSurname = () => localStorage.getItem('authToken').split(" ")[2];

    changeUsername() {

        var newNick = (<HTMLInputElement>document.getElementById("username")).value;
        if (newNick.length < 3) {
            document.getElementById("serverAnswer").innerHTML = "New username is too short!"
            return;
        } else if (newNick.length > 16) {
            document.getElementById("serverAnswer").innerHTML = "New username is too long!"
            return;
        }

        var xhttp = new XMLHttpRequest();

        document.getElementById("serverAnswer").innerHTML = newNick;
        /*
         var params = JSON.stringify({
         type: "changenick",
         newnick: newNick
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
        */
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

        document.getElementById("serverAnswer").innerHTML = newName + " " + newSurname;

        var xhttp = new XMLHttpRequest();
        /*
        var params = JSON.stringify({
            type: "changename",
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
        */
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

        /*
        var params = JSON.stringify({
            type: "changepwd",
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
        */
    }


}
