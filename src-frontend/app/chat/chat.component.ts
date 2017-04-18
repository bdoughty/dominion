import {Component, OnInit} from '@angular/core';
import {Chat} from "./chat.model";
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
    private _chatSocketService: ChatSocketService
  ) {}

  ngOnInit() {
    this._chatSocketService.addListener('chat', (messageString) => {
      let message = JSON.parse(messageString);
      this.chatModel.addMessage(message);

      console.log("recieving");
      console.log(message);
    });
  }

  onEnter(value) {

    console.log("sending");
    console.log(value);

    this._chatSocketService.send('chat', value);
    this.currentMessage = "";
  }
}
