import {Component, OnInit} from '@angular/core';
import {Chat} from "./chat.model";
import {UserIdService} from "../shared/user-id.service";
import {SocketService} from "../shared/socket.service";

@Component({
  selector: 'dmn-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  private chatModel = new Chat();
  public currentMessage: string;

  constructor(
    private _userIdService: UserIdService,
    private _socketService: SocketService
  ) {
    this.chatModel.addMessage({
      name: 'Another',
      color: '#333444',
      message: 'Another Message message'
    });
    this.chatModel.addMessage({
      name: 'Testing',
      color: '#333444',
      message: 'Testing message'
    });
  }

  ngOnInit() {
    this._socketService.addListener('chat', (message) => {
      this.chatModel.addMessage({name: 'Testing', color: 'Testing', message: message});
    });
  }

  onEnter(value) {
    console.log(value);

    this.chatModel.addMessage({
      name: 'Testing',
      color: 'Testing',
      message: value
    });

    this.currentMessage = "";
  }
}
