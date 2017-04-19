import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import {LobbyComponent} from "./lobby/lobby.component";
import {CreationComponent} from "./creation-page/creation-page.component";
import {GameComponent} from "./game/game.component";
import {UserIdService} from "./shared/user-id.service";
import {GameService} from "./game/game.service";
import {appRoutes} from "./routes";
import {ChatComponent} from "./chat/chat.component";
import {ChatSocketService} from "./shared/chatsocket.service";
import {GameSocketService} from "./shared/gamesocket.service";
import {CardComponent} from "./game/card/card.component";

@NgModule({
  declarations: [
    AppComponent,
    LobbyComponent,
    CreationComponent,

    GameComponent,
    LobbyComponent,
    CreationComponent,

    ChatComponent,
    CardComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    UserIdService,
    GameService,
    ChatSocketService,
    GameSocketService
    // SocketService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
