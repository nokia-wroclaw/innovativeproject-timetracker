import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {LoginComponent} from "./login/login.component";
import {FormsModule}   from "@angular/forms";
@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [LoginComponent],
    bootstrap: [LoginComponent]
})
export class LoginModule extends Type {
    constructor() {
        super();
    }
}
