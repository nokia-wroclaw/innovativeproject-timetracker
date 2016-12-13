import
{ Component } from "@angular/core";
import {UserService} from "./user.service";

@Component({
    selector: 'menu-comp',
    templateUrl: 'menu.template.html',
    styleUrls: ["./menu.style.scss".toString()],
    providers: [UserService]
})

export class MenuComponent {
    userService = new UserService();

    constructor() {
    }

    logout() {
        this.userService.logoutRequest();

        //TODO: Delete this line when server will start to do it itself
        window.location.href = '/login';
    }

    userName = () => localStorage.getItem('authToken').split(" ")[1];
    userSurname = () => localStorage.getItem('authToken').split(" ")[2];

    changeToTimelinePage(){
        window.location.href = 'http://localhost:9000/';
    }

}


