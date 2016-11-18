import { Component } from "@angular/core";
import { User } from "../_common/user";
import { NgForm } from "@angular/forms";
import { UserService } from "../_common/user.service";
@Component({
    selector: 'login-comp',
    templateUrl: 'login.template.html',
    styleUrls: ["./login.style.scss".toString()],
    providers: [UserService]
})

export class LoginComponent {
    model = new User('', '');
    submitted = false;
    userService = new UserService();
    constructor() {
    }

    onSubmit() {
        this.submitted = true;
    }

    sendLoginRequest(logForm:NgForm) {
        this.userService.loginRequest(logForm.value.login, logForm.value.pwd);

        //  DEPRECATED

        /*var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            login: logForm.value.login,
            password: logForm.value.pwd
        });
        xhttp.open("POST", "http://localhost:9000/login", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("loginServerAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send(params);
        */
    }

}
