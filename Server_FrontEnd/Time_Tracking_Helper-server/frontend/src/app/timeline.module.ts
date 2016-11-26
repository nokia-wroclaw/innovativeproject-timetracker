import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {TimelineComponent} from "./timeline/timeline.component";


@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [TimelineComponent],
    bootstrap: [TimelineComponent]
})
export class TimelineModule extends Type {
    constructor() {
        super();
    }
}
