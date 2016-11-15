import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {RegisterComponent} from "./register/register.component";


@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [RegisterComponent],
    bootstrap: [RegisterComponent]
})
export class RegisterModule extends Type {
    constructor() {
        super();
    }
}
