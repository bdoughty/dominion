import {Injectable} from "@angular/core";
import {MessageService} from "./message-functions.service";
import {UserIdService} from "./user-id.service";

@Injectable()
export class SocketService {
  public sock = new WebSocket("ws://" + location.hostname + ":" + location.port + "/socket");
  public userId;

  constructor(public _messageService: MessageService,
              public _userIdService: UserIdService) {

    this.sock.onopen = () => {
      if (this.getCookie("id") != null) {
        this.userId = this.getCookie("id");
        this.sock.send("oldid:" + this.userId);
        console.log("User ID Remembered: " + this.userId);
      } else {
        this.sock.send("newid:");
      }
    };

    this.sock.onmessage = (event) => {
      const str = event.data;
      const semi = str.indexOf(':');
      const type = str.substring(0, semi);
      if (str.length > semi + 1) {
        const message = str.substring(semi + 1);
        this._messageService[type](message);
      } else {
        this._messageService[type]();
      }
    };
  }

  public send(type, message) {
    this.sock.send(type + ":" + this.userId + ":" + message);
  }

  public getCookie(name: string) {
    const value = "; " + document.cookie;
    const parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
  }
}

// messageFuntions.userid = function (id) {
//   document.cookie = "id = " + id;
//   console.log("User ID Received: " + id);
// };


