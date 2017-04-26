import {Component, OnInit} from '@angular/core';
import {ChatSocketService} from "../shared/chatsocket.service";

@Component({
  selector: 'dmn-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit {
  public pendings;
  public selectedGame;
  public selectedGameId;
  public joiningGame = false;

  constructor(private _chatSocketService: ChatSocketService) {
  }

  ngOnInit() {
    this._chatSocketService.addListener('pending', (messageString) => {
      this.pendings = JSON.parse(messageString);
      console.log("pending updated");
      console.log(this.pendings);

      // Update selected game by searching through new games
      let foundGameInNewGames = false;
      if (this.selectedGameId !== undefined) {
        this.pendings.forEach((pendingGame) => {
          if (pendingGame.id === this.selectedGameId) {
            foundGameInNewGames = true;
            this.selectedGame = pendingGame;
          }
        });
      }
      if (!foundGameInNewGames) {
        this.selectedGame = undefined;
      }
    });

    this._chatSocketService.addListener('redirect', (messageString) => {
      window.location.replace(messageString);
    });
  }

  select(pending) {
    this.selectedGame = pending;
    this.selectedGameId = pending.id;
  }

  leave() {
    this._chatSocketService.send('leave', "");
  }

  join() {
    this._chatSocketService.send('join',
      JSON.stringify({gameid: this.selectedGame.id}));
  }
}
