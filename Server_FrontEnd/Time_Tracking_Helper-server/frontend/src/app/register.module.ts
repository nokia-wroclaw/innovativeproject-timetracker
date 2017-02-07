import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {RegisterComponent} from "./register/register.component";
import {FormsModule}   from "@angular/forms";


@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [RegisterComponent],
    bootstrap: [RegisterComponent]
})
export class RegisterModule extends Type {
    constructor() {
        super();
    }
}
