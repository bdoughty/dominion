import {Player} from "./player.model";
import {Card} from "../card/card.model";
export class ClientGame {

  public turn: number = 0;
  public holding: boolean = false;
  public cart = [];
  public hand: Card[] = [];
  public discardsize: number = 0;
  public decksize: number = 10;
  public toSelect: boolean = false;
  public toSelectHand = [];
  public toSelectBoard = [];
  public phase: string = "action";

  public nonactionCards: Card[] =
    [new Card(0), new Card(1), new Card(2), new Card(3), new Card(4), new Card(5)];

  constructor(public players: Player[],
              public ownPlayerId: number,
              public actionCards: Card[]) {
  }

  public addToCart(id: number): void {
    let card = new Card(id);
    if (this.canBuy(card)) {
      this.cart.push(card);
      this.gold -= card.cost;
      this.buys -= 1;
    }
  }

  public removeFromCart(card: Card): void {
    this.gold += card.cost;
    this.buys += 1;
    this.cart.splice(this.cart.indexOf(card), 1);
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

  public canBuy(card: Card) {
    return (this.phase !== 'buy') || card.cost <= this.gold && this.buys > 0;
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
