import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {IndexComponent} from "./index/index.component";
import {HeaderComponent} from "./_common/header.component";

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [IndexComponent, HeaderComponent],
    bootstrap: [IndexComponent, HeaderComponent]
})
export class IndexModule extends Type {
    constructor() {
        super();
    }
}
