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
  public inGame = false;
  public searchText = "";

  constructor(public _chatSocketService: ChatSocketService) {
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

  gameMatchesSearch(game): boolean {
    if (this.searchText == "") {
      return true;
    } else {
      return game.name.toLowerCase().includes(this.searchText.toLowerCase());
    }
  }

  select(pending) {
    if (!this.inGame) {
      this.selectedGame = pending;
      this.selectedGameId = pending.id;
    }
  }

  leave() {
    this.inGame = false;
    this._chatSocketService.send('leave', "");
  }

  join() {
    this.inGame = true;
    this._chatSocketService.send('join',
      JSON.stringify({gameid: this.selectedGame.id}));
  }
}
