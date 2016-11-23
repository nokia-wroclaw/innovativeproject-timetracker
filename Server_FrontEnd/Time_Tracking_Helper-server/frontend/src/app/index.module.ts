import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {IndexComponent} from "./index/index.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";

@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [IndexComponent, HeaderComponent, MenuComponent],
    bootstrap: [IndexComponent, HeaderComponent, MenuComponent]
})
export class IndexModule extends Type {
    constructor() {
        super();
    }
}
