import {Pile} from "./pile.model";

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

  constructor(public players,
              public pileArray: Pile[],
              public turnList,
              public ownPlayerId) {
  }

  getSelf() {
    return this.players[this.ownPlayerId];
  }

  getCurrentPlayer() {
    return this.players[this.turnList[this.turn]];
  }
}
