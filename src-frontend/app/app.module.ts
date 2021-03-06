import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import {LobbyComponent} from "./lobby/lobby.component";
import {CreationComponent} from "./creation-page/creation-page.component";
import {GameComponent} from "./game/game.component";
import {UserIdService} from "./shared/user-id.service";
import {appRoutes} from "./routes";
import {ChatComponent} from "./chat/chat.component";
import {ChatSocketService} from "./shared/chatsocket.service";
import {GameSocketService} from "./shared/gamesocket.service";
import {CardComponent} from "./game/card/card.component";
import {NotificationComponent} from "./shared/notification/notification.component";
import {FillPipe} from "./shared/fill.pipe";
import {NamePageComponent} from "./name-page/name-page.component";
import {NameSocketService} from "./shared/namesocket.service";

@NgModule({
  declarations: [
    AppComponent,
    LobbyComponent,
    CreationComponent,

    GameComponent,
    LobbyComponent,
    CreationComponent,

    NamePageComponent,

    ChatComponent,
    CardComponent,
    NotificationComponent,

    FillPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    UserIdService,
    ChatSocketService,
    GameSocketService,
    NameSocketService
    // SocketService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
