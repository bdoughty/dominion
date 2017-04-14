import {Component, OnInit} from '@angular/core';
import {Chat} from "./chat.model";
import {UserIdService} from "../shared/user-id.service";
import {ChatSocketService} from "../shared/chatsocket.service";

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
    private _chatSocketService: ChatSocketService
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
    this._chatSocketService.addListener('chat', (message) => {
      this.chatModel.addMessage({name: 'Testing', color: 'Testing', message: message});
    });
  }

  onEnter(value) {
    this._chatSocketService.send('chat', value);
    this.currentMessage = "";
  }
}
