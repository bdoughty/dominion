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
  private needToUpdate = false;
  public currentMessage: string;

  constructor(
    private _chatSocketService: ChatSocketService
  ) {}

  ngOnInit() {
    this._chatSocketService.addListener('chat', (messageString) => {
      let message = JSON.parse(messageString);
      this.chatModel.addMessage(message);

      this.needToUpdate = true;
    });
  }

  onEnter(value) {
    this._chatSocketService.send('chat', value);
    this.currentMessage = "";
  }

  updateScroll() {
    if (this.needToUpdate) {
      document.getElementById("message" +
        (this.chatModel.messages.length - 1)).scrollIntoView(true);
    }
    this.needToUpdate = false;
  }
}
