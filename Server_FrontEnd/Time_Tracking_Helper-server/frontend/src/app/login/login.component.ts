import { Component } from "@angular/core";
import { User } from "../_common/user";
import {NgForm} from "@angular/forms";
@Component({
    selector: 'login-comp',
    templateUrl: 'login.template.html',
    styleUrls: ["./login.style.scss".toString()]
})

export class LoginComponent {
    model = new User('', '');
    submitted = false;

    constructor() {
    }

    onSubmit() {
        this.submitted = true;
    }

    sendLoginRequest(logForm:NgForm) {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            login: logForm.value.login,
            password: logForm.value.pwd
        });
        xhttp.open("POST", "http://localhost:9000/login", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("loginServerAnswer").innerHTML = xhttp.responseText;
               /* if(xhttp.responseText == "ADDED") {
                    document.getElementById("loginServerAnswer").innerHTML = "Registration successful";
                } else {
                    document.getElementById("loginServerAnswer").innerHTML = "Unsuccessful registration, please try again!";
                }*/
            }
        };
        xhttp.send(params);
    }

}
