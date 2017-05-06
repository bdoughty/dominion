import {Player} from "./player.model";
import {Card} from "../card/card.model";
import {PlayerAction} from "./player-action.model";
export class ClientGame {
  public turn: number = 0;
  public holding: boolean = false;
  public cart = [];
  public hand: Card[] = [];
  public discardsize: number = 0;
  public decksize: number = 10;
  public toSelectHand = [];
  public toSelectBoard = [];
  public toSelectStoppable = false;
  public phase: string = "action";
  public orderedPlayers: Player[];
  public isOver: boolean = false;
  public playerActionQueue: PlayerAction[] = [];

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

  public isDisabled(card: Card, inHand: boolean): boolean {
    if (this.isSelectable(card, inHand)) {
      return false;
    }

    if (inHand) {
      return !this.canPlay(card);
    } else {
      return card.pileCount == 0 || !this.canBuy(card);
    }
  }

  public isSelectable(card: Card, inhand: boolean): boolean {
    const currPlayerAction: PlayerAction = this.getCurrPlayerAction();
    if (currPlayerAction) {
      if (inhand) {
        return currPlayerAction.handSelect.indexOf(card.id) !== -1;
      } else {
        return currPlayerAction.boardSelect.indexOf(card.id) !== -1;
      }
    }
    return false;
  }

  public winner(): Player {
    let winner = this.players[0];
    let max = winner.victoryPoints;
    this.players.forEach(player => {
      if (player.victoryPoints > max)  {
        winner = player;
      }
    });
    return winner;
  }

  public getCurrPlayerAction(): PlayerAction {
    if (this.playerActionQueue.length == 0) {
      return null;
    } else {
      return this.playerActionQueue[0];
    }
  }

  public updatePiles(piles: any): void {
    const allCards = this.nonactionCards.concat(this.actionCards);
    allCards.forEach(card => {
      if (piles[card.id]) {
        card.pileCount = piles[card.id]['size'];
      }
    });
  }

  public getPlayerById(id: number): Player {
    for (let i = 0; i < this.players.length; i++) {
      if (this.players[i].id == id) {
        return this.players[i];
      }
    }
    throw "Could not find player with id " + id;
  }

  public addToCart(card: Card): void {
    const cardToAdd = new Card(card.id);
    if (this.canBuy(card)) {
      this.cart.push(cardToAdd);
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

  public isSelecting(): boolean {
    if (this.getCurrPlayerAction() == null) {
      return false;
    }
    return this.getCurrPlayerAction().requiresSelect;
  }

  public canEndPhase(): boolean {
    return this.isOwnTurn()
      && !this.isSelecting()
      && !this.isOver;
  }

  public canPlay(card: Card) {
    return this.isOwnTurn()
      && !this.isSelecting()
      && this.actions > 0
      && (card.type === 'action' || card.type === 'reaction');
  }

  public canBuy(card: Card) {
    console.log(card.pileCount);
    return card.pileCount > 0 && (this.phase === 'buy') && card.cost <= this.gold && this.buys > 0;
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
