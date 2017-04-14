import {Routes} from "@angular/router";
import {LobbyComponent} from "./lobby/lobby.component";
import {GameComponent} from "./game/game.component";
import {CreationComponent} from "./creation-page/creation-page.component";

export const appRoutes: Routes = [
  {
    path: 'lobby',
    component: LobbyComponent
  },
  {
    path: 'game',
    component: GameComponent
  },
  {
    path: 'create',
    component: CreationComponent
  },
  { path: '',
    redirectTo: '/lobby',
    pathMatch: 'full'
  },
  {
    path: '**',
    component: LobbyComponent
  }
];
