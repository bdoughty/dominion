import {AbstractSocketService} from "./socket.service";
import {Injectable} from "@angular/core";
import {UserIdService} from "./user-id.service";

@Injectable()
export class NameSocketService extends AbstractSocketService {
  constructor(public _userIdService: UserIdService) {
    super(_userIdService, "/name");
  }
}
