import {Component, Input, OnInit} from '@angular/core';
import {Chat} from "./chat.model";
import {ChatSocketService} from "../shared/chatsocket.service";
import {GameSocketService} from "../shared/gamesocket.service";
import {AbstractSocketService} from "../shared/socket.service";

@Component({
  selector: 'dmn-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  @Input() public endpoint = 'homechat';
  private chatModel = new Chat();
  private needToScroll = false;
  public currentMessage: string;
  @Input() public socket: AbstractSocketService;


  constructor() {}

  ngOnInit() {
    this.socket.addListener('chat', (messageString) => {
      let message = JSON.parse(messageString);
      this.chatModel.addMessage(message);

      this.needToScroll = true;
    });
  }

  onEnter(value) {
    this.socket.send('chat', value);
    this.currentMessage = "";
  }

  updateScroll() {
    if (this.needToScroll) {
      document.getElementById("message" +
        (this.chatModel.messages.length - 1)).scrollIntoView(true);
    }
    this.needToScroll = false;
  }
}
