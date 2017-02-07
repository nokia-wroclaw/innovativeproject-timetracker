import { Component } from "@angular/core";
import { User } from "./user";
import { UserService } from "./user.service";
@Component({
    selector: 'header-comp',
    templateUrl: 'header.template.html',
    styleUrls: ["./_common.style.scss".toString()]
})

export class HeaderComponent {

    constructor() {
    }

    changeToTimelinePage(){
        window.location.href = 'http://localhost:9000/';
    }

}
