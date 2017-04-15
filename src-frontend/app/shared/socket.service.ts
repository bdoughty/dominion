import {UserIdService} from "./user-id.service";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs/Observable";

export class AbstractSocketService {
  public sock;
  public userId;
  public messages: Observable<string>;
  public listeners = {};

  constructor(public _userIdService: UserIdService, private endpoint: string) {
    this.sock = new WebSocket("ws://" + location.hostname + ":" + environment.port + endpoint);

    this.addListener('userid', (userId) => {
      _userIdService.id = userId;
      document.cookie = "id=" + userId;
      console.log("Loaded id " + userId);
    });

    this.sock.onopen = () => {
      if (this.getCookie("id") != null) {
        this.userId = this.getCookie("id");
        this.sock.send("oldid:" + this.userId);
      } else {
        this.sock.send("newid:");
      }
    };

    this.sock.onmessage = (event) => {
      const str = event.data;
      const semi = str.indexOf(':');
      const type = str.substring(0, semi);

      if (this.listeners[type]) {
        if (str.length > semi + 1) {
          const message = str.substring(semi + 1);
          this.listeners[type].forEach((func) => func(message));
        } else {
          this.listeners[type].forEach((func) => func());
        }
      }
    }
  }

  public addListener(name: string, func: Function) {
    if (!this.listeners[name]) {
      this.listeners[name] = [func];
    } else {
      this.listeners[name].push(func);
    }
  }

  public send(type, message) {
    this.sock.send(type + ":" + message);
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


