import {Component} from "@angular/core";

@Component({
    selector: 'index-comp',
    templateUrl: 'index.template.html',
    styleUrls: ["./index.style.scss".toString()]
})

export class IndexComponent {
    constructor() {
    }
    
    hello = () => "Hello!"
}
