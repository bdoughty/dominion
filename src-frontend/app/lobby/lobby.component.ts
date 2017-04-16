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
  }

  select(pending) {
    this.selectedGame = pending;
  }
}
