import {Player} from "./player.model";
import {Card} from "./card.model";
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

  public nonactionCards: Card[] = [];
  public actionCards: number[] = [1,2,3,4,5,6,7,8,9,10];

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

  get buys() {
    return this.getOwnPlayer().buys;
  }

  set buys(buys: number) {
    this.getOwnPlayer().buys = buys;
  }

  get actions() {
    return this.getOwnPlayer().actions;
  }

  set actions(actions: number) {
    this.getOwnPlayer().actions = actions;
  }

  get gold() {
    return this.getOwnPlayer().gold;
  }

  set gold(gold: number) {
    this.getOwnPlayer().gold = gold;
  }
}
