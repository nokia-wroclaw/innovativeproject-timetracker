import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {ScheduleComponent} from "./schedule/schedule.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";
import {FormsModule}   from "@angular/forms";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [ScheduleComponent, HeaderComponent, MenuComponent],
    bootstrap: [ScheduleComponent, HeaderComponent, MenuComponent]
})
export class ScheduleModule extends Type {
    constructor() {
        super();
    }
}
