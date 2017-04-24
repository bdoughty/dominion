import {AbstractSocketService} from "./socket.service";
import {Injectable} from "@angular/core";
import {UserIdService} from "./user-id.service";

@Injectable()
export class GameSocketService extends AbstractSocketService {
  constructor(public _userIdService: UserIdService) {
    super(_userIdService, "/gamesocket");
  }
}
