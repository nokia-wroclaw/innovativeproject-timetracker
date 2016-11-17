import { Component } from "@angular/core";
import { User } from "../_common/user";

@Component({
    selector: 'login-comp',
    templateUrl: 'login.template.html',
    styleUrls: ["./login.style.scss".toString()]
})

export class LoginComponent {
    model = new User('', '');
    submitted = false;

    onSubmit() {
        this.submitted = true;
    }
    constructor() {
    }
}