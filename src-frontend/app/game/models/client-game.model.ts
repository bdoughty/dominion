import {Player} from "./player.model";
import {Card} from "./card.model";
export class ClientGame {

  public turn: number = 0;
  public gamePhase: string = "action";
  public holding: boolean = false;
  public toBuy = [];
  public deck: number = 10;
  public hand: Card[] = [];
  public discardPile: number = 0;
  public toSelect: boolean = false;
  public toSelectHand = [];
  public toSelectBoard = [];

  public nonactionCards: Card[] =
    [new Card(0), new Card(1), new Card(2), new Card(3), new Card(4), new Card(5)];
  public actionCards: Card[] = [];

  constructor(public players: Player[],
              public ownPlayerId: number,
              actionCards: number[]) {
    actionCards.forEach((id) => {
      this.actionCards.push(new Card(id));
    })
  }

  public setTurn(id: number) {
    for (let i = 0; i<this.players.length; i++) {
      if (id == this.players[i].id) {
        this.turn = i;
      }
    }
  }

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
