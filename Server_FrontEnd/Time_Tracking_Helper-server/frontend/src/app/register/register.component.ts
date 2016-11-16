import { Component } from "@angular/core";
import { User } from "../_common/user.ts";

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

    sendUser() {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({password: "lol", login: "Kruk0711", name: "Janusz", surname: "Paciaciak", email: "asdasdads@k.pl"});
        xhttp.open("POST", "http://localhost:9000/addUser", true);
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("demo").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send();
    }
    getUsers() {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "http://localhost:9000/users", true);
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("demo").innerHTML = xhttp.responseText;
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
