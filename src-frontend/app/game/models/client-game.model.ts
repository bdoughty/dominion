import {Player} from "./player.model";
import {Card} from "../card/card.model";
export class ClientGame {

  public turn: number = 0;
  public phase: string = "action";
  public holding: boolean = false;
  public cart = [];
  public hand: Card[] = [];
  public discardsize: number = 0;
  public decksize: number = 10;
  public toSelect: boolean = false;
  public toSelectHand = [];
  public toSelectBoard = [];


  public nonactionCards: Card[] =
    [new Card(0), new Card(1), new Card(2), new Card(3), new Card(4), new Card(5)];

  constructor(public players: Player[],
              public ownPlayerId: number,
              public actionCards: Card[]) {
  }

  public addToCart(id: number): void {
    this.cart.push(new Card(id));
  }

  public setTurn(id: number): void {
    for (let i = 0; i < this.players.length; i++) {
      if (id == this.players[i].id) {
        this.turn = i;
      }
    }
  }

  public isOwnTurn(): boolean {
    return this.players[this.turn].id === this.ownPlayerId;
  }

  public getOwnPlayer(): Player {
    for (let i = 0; i<this.players.length; i++) {
      if (this.players[i].id == this.ownPlayerId) {
        return this.players[i];
      }
    }
  }

  public removeCardInHand(card: Card): void {
    this.hand.splice(this.hand.indexOf(card), 1);
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
