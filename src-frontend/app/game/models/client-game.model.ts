import {Player} from "./player.model";
import {Card} from "../card/card.model";
export class ClientGame {

  public turn: number = 0;
  public holding: boolean = false;
  public cart = [];
  public hand: Card[] = [];
  public discardsize: number = 0;
  public decksize: number = 10;
  public isSelecting: boolean = false;
  public toSelectHand = [];
  public toSelectBoard = [];
  public toSelectStoppable = false;
  public phase: string = "action";
  public orderedPlayers: Player[];

  public nonactionCards: Card[] =
    [new Card(0), new Card(1), new Card(2), new Card(3), new Card(4), new Card(5)];

  constructor(public players: Player[],
              public ownPlayerId: number,
              public actionCards: Card[]) {
    this.orderedPlayers = [];
    this.players.forEach(player => {
      this.orderedPlayers.push(player);
    });
  }

  public updatePiles(piles: any) {
    const allCards = this.nonactionCards.concat(this.actionCards);
    allCards.forEach(card => {
      if (piles[card.id]) {
        card.pileCount = piles[card.id];
      }
    });
  }

  public isSelectable(card: Card, inhand: boolean) {
    if (inhand) {
      return this.toSelectHand.indexOf(card.id) !== -1;
    } else {
      return this.toSelectBoard.indexOf(card.id) !== -1;
    }
  }

  // Used to make selecting instant so multiple selections cannot be registered.
  public setNotSelecting() {
    this.toSelectHand = [];
    this.toSelectBoard = [];
  }

  public getPlayerById(id: number) {
    for (let i = 0; i < this.players.length; i++) {
      if (this.players[i].id == id) {
        return this.players[i];
      }
    }
    throw "Could not find player with id " + id;
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
    let found = false;
    for (let i = 0; i < this.players.length; i++) {
      if (id === this.players[i].id) {
        this.turn = i;
        found = true;
      }
    }
    if (!found) {
      throw "Could not find player id in turns";
    }

    // Cycle ordered players list for the display
    this.orderedPlayers = [];
    for (let i = this.turn; i < this.players.length + this.turn; i++) {
      let index = i % this.players.length;
      this.orderedPlayers.push(this.players[index]);
    }
  }

  public isOwnTurn(): boolean {
    return this.players[this.turn].id === this.ownPlayerId;
  }

  public getOwnPlayer(): Player {
    for (let i = 0; i<this.players.length; i++) {
      if (this.players[i].id === this.ownPlayerId) {
        return this.players[i];
      }
    }
    throw "Could not find own player";
  }


  public removeCardInHand(card: Card): void {
    this.hand.splice(this.hand.indexOf(card), 1);
  }

  public canPlay(card: Card) {
    return this.isOwnTurn()
      && !this.isSelecting
      && this.actions > 0
      && (card.type === 'action' || card.type === 'reaction');
  }

  public canBuy(card: Card) {
    return (this.phase === 'buy') && card.cost <= this.gold && this.buys > 0;
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
