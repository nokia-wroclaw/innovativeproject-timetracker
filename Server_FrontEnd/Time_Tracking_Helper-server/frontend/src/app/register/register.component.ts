import { Component } from "@angular/core";
import { User } from "../_common/user.ts";
import {FormsModule, NgForm} from "@angular/forms";

@Component({
    selector: 'register-comp',
    templateUrl: 'register.template.html',
    styleUrls: ["./register.style.scss".toString()]
})

export class RegisterComponent {
    model = new User('', '', '', '', '');
    submitted = false;

    onSubmit() {
        this.submitted = true;
    }

    sendUser(regForm:NgForm) {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            login: regForm.value.login,
            password: regForm.value.pwd,
            name: regForm.value.name,
            surname: regForm.value.surname,
            email: regForm.value.email
        });
        xhttp.open("POST", "http://localhost:9000/addUser", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                //document.getElementById("demo").innerHTML = xhttp.responseText;
                if(xhttp.responseText == "ADDED") {
                    window.location.href = 'http://localhost:9000/login';
                } else {
                    document.getElementById("registerServerAnswer").innerHTML = "Unsuccessful registration, please try again!";
                }
            }
        };
        xhttp.send(params);
    }
    getUsers() {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "http://localhost:9000/users", true);
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("registerServerAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send();
    }

    get diagnostic() {
        return JSON.stringify(this.model);
    }
    constructor() {
    }
}
