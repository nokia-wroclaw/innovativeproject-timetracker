import { Component } from "@angular/core";
import { User } from "./user";
import { UserService } from "./user.service";
@Component({
    selector: 'header-comp',
    templateUrl: 'header.template.html',
    styleUrls: ["./_common.style.scss".toString()],
    providers: [UserService]
})

export class HeaderComponent {
    userService = new UserService();

    constructor() {
    }

    logout() {
        this.userService.logoutRequest();

        //TODO: Delete this line when server will start to do it itself
        window.location.href = 'http://localhost:9000/login';
    }

    userName = () => localStorage.getItem('authToken').split(" ")[1];
    userSurname = () => localStorage.getItem('authToken').split(" ")[2];
}
