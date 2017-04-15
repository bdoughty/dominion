import { Injectable } from '@angular/core';
import {UserIdService} from "../shared/user-id.service";

@Injectable()
export class GameService {

  constructor(
    private _userIdService: UserIdService) {}

  //TODO, maybe not the best way to store / load this method
  public updateMapFromJson(response: string) {
    // this._messageService.updateMap(response);
  }

  public updateMapPost(url: string, params: any) {
    // $.post("/" + url, params, response => {
    //   this.updateMapFromJson(response);
    // });
  }

  public sendBuys(buys) {
    const postParams = {userId: this._userIdService.id, boughtCards: buys};
    this.updateMapPost("buys", postParams);
  }

  public sendAction(actionCardId) {
    const postParams = {userId: this._userIdService.id, cardId: actionCardId};
    this.updateMapPost("action", postParams);
  }
}
