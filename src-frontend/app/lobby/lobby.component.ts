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
  public joiningGame = false;

  constructor(private _chatSocketService: ChatSocketService) {
  }

  ngOnInit() {
    this._chatSocketService.addListener('pending', (messageString) => {
      this.pendings = JSON.parse(messageString);
    });

    this._chatSocketService.addListener('joinresponse', (messageString) => {
      console.log(JSON.parse(messageString));
    });

    this._chatSocketService.addListener('leaveresponse', (a) => {});
  }

  select(pending) {
    this.selectedGame = pending;
  }

  leave() {
    this._chatSocketService.send('leave', "");
    console.log("left");
  }

  join() {
    console.log(this.selectedGame);
    this._chatSocketService.send('join',
      JSON.stringify({gameid: this.selectedGame.id}));
  }
}
