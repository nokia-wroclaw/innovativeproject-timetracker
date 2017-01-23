import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {SettingsComponent} from "./settings/settings.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";
import {FormsModule}   from "@angular/forms";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [SettingsComponent, HeaderComponent, MenuComponent],
    bootstrap: [SettingsComponent, HeaderComponent, MenuComponent]
})
export class SettingsModule extends Type {
    constructor() {
        super();
    }
}
