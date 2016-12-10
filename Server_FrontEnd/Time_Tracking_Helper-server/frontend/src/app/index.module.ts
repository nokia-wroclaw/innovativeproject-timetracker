import {Type, NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {IndexComponent} from "./index/index.component";
import {HeaderComponent} from "./_common/header.component";
import {MenuComponent} from "./_common/menu.component";
import {FormsModule}   from "@angular/forms";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule
    ],
    declarations: [IndexComponent, HeaderComponent, MenuComponent],
    bootstrap: [IndexComponent, HeaderComponent, MenuComponent]
})
export class IndexModule extends Type {
    constructor() {
        super();
    }
}
