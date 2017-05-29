import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {MenuComponent} from './components/menu/menu.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {MarkdownModule} from 'angular2-markdown';

@NgModule({
    declarations: [
        AppComponent,
        MenuComponent,
        DashboardComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        MarkdownModule.forRoot()
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
