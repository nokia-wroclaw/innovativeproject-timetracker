import { Component } from "@angular/core";
import { User } from "../_common/user";
import {FormsModule, NgForm} from "@angular/forms";

@Component({
    selector: 'register-comp',
    templateUrl: 'register.template.html',
    styleUrls: ["./register.style.scss".toString()]
})

export class RegisterComponent {
    model = new User('', '', '', '', '', '');
    submitted = false;

    onSubmit() {
        this.submitted = true;
    }

    sendUser(regForm:NgForm) {

        if(regForm.value.pwd != regForm.value.pwdR) {
            document.getElementById("registerServerAnswer").innerHTML =  "Mismatch passwords!";
            return;
        }

        if(regForm.value.pwd.length < 6) {
            document.getElementById("registerServerAnswer").innerHTML = "Password must be at least 6 characters long!"
            return;
        }

        if (regForm.value.name.length == 0) {
            document.getElementById("registerServerAnswer").innerHTML = "No name given!"
            return;
        } else if (regForm.value.name.length > 32) {
            document.getElementById("registerServerAnswer").innerHTML = "New name is too long!"
            return;
        }

        if (regForm.value.surname.length == 0) {
            document.getElementById("registerServerAnswer").innerHTML = "No surname given!"
            return;
        } else if (regForm.value.surname.length > 64) {
            document.getElementById("registerServerAnswer").innerHTML = "New surname is too long!"
            return;
        }

        if (regForm.value.login.length < 3) {
            document.getElementById("registerServerAnswer").innerHTML = "New username is too short!"
            return;
        } else if (regForm.value.login.length > 16) {
            document.getElementById("registerServerAnswer").innerHTML = "New username is too long!"
            return;
        }

        //validate email
        var emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if(emailRegex.test(regForm.value.email) == false) {
            document.getElementById("registerServerAnswer").innerHTML = "Invalid e-mail!"
            return;
        }
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            login: regForm.value.login,
            password: regForm.value.pwd,
            name: regForm.value.name,
            surname: regForm.value.surname,
            email: regForm.value.email
        });
        xhttp.open("POST", "/addUser", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                //document.getElementById("demo").innerHTML = xhttp.responseText;
                if(xhttp.responseText == "ADDED") {
                    window.location.href = '/login';
                } else {
                    document.getElementById("registerServerAnswer").innerHTML = "Unsuccessful registration, please try again!";
                }
            }
        };
        xhttp.send(params);
    }
    getUsers() {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "/users", true);
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
    changeToLoginPage(){
        window.location.href = 'http://localhost:9000/login';
    }
}
