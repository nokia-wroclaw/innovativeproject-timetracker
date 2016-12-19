import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {PrivilegesComponent} from "./privileges/privileges.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";
import {FormsModule}   from "@angular/forms";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [PrivilegesComponent, HeaderComponent, MenuComponent],
    bootstrap: [PrivilegesComponent, HeaderComponent, MenuComponent]
})
export class PrivilegesModule extends Type {
    constructor() {
        super();
    }
}