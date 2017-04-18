import {Player} from "./player.model";
export class ClientGame {

  public turn: number = 0;
  public gamePhase: string = "action";
  public holding: boolean = false;
  public toBuy = [];
  public deck: number = 10;
  public hand = [];
  public discardPile: number = 0;
  public toSelect: boolean = false;
  public toSelectHand = [];
  public toSelectBoard = [];

  constructor(public players: Player[],
              public ownPlayerId) {

  }

  // getCurrentPlayer() {
  //   return this.players[this.players[this.turn]];
  // }

  getOwnPlayer() {
    for (let i = 0; i<this.players.length; i++) {
      if (this.players[i].id == this.ownPlayerId) {
        return this.players[i];
      }
    }
  }

  getBuys() {
    return this.getOwnPlayer().buys;
  }

  getActions() {
    return this.getOwnPlayer().actions;
  }

  getGold() {
    return this.getOwnPlayer().gold;
  }
}
