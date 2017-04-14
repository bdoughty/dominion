import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import {LobbyComponent} from "./lobby/lobby.component";
import {CreationComponent} from "./creation-page/creation-page.component";
import {GameComponent} from "./game/game.component";
import {UserIdService} from "./shared/user-id.service";
import {GameService} from "./game/game.service";
import {SocketService} from "./shared/socket.service";

@NgModule({
  declarations: [
    AppComponent,
    LobbyComponent,
    CreationComponent,

    GameComponent,
    LobbyComponent,
    CreationComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    UserIdService,
    GameService,
    SocketService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
