import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {TimelineComponent} from "./timeline/timeline.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";
import {FormsModule}   from "@angular/forms";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [TimelineComponent, HeaderComponent, MenuComponent],
    bootstrap: [TimelineComponent, HeaderComponent, MenuComponent]
})
export class TimelineModule extends Type {
    constructor() {
        super();
    }
}
