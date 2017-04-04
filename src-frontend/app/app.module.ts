import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import {LobbyComponent} from "./lobby/lobby.component";
import {CreationComponent} from "./creation-page/creation-page.component";

@NgModule({
  declarations: [
    AppComponent,
    LobbyComponent,
    CreationComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
