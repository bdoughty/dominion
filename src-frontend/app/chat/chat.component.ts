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
  private _socketService: AbstractSocketService;

  constructor(
    private _chatSocketService: ChatSocketService,
    private _gameSocketService: GameSocketService
  ) {
    if (this.endpoint === 'game') {
      this._socketService = _gameSocketService;
    } else {
      this._socketService = _chatSocketService;
    }
  }

  ngOnInit() {
    this._chatSocketService.addListener('chat', (messageString) => {
      let message = JSON.parse(messageString);
      this.chatModel.addMessage(message);

      this.needToScroll = true;
    });
  }

  onEnter(value) {
    this._socketService.send('chat', value);
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
