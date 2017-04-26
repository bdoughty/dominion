import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./card/card.model";
import {trigger, state, style, animate, transition, keyframes} from "@angular/animations";

@Component({
  selector: 'dmn-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
  animations: [
    trigger('cardState', [
      state('inhand', style({})),
      state('played', style({
        display: 'none'
      })),
      transition('inhand => played', [
        animate('1000ms ease', keyframes([
          style({position: 'relative', top: '0', offset: 0}),
          style({top: '-500px', position: 'relative', opacity: '0', offset: 0.5}),
          style({width: '0', offset: 0.8})
        ]))
      ])
    ])
  ]
})
export class GameComponent implements OnInit {
  public title = 'Dominion';
  public game: ClientGame;
  public gameChat = new Chat();

  constructor(
    private _userIdService: UserIdService,
    private _gameSocketService: GameSocketService
  ) {}

  ngOnInit(): void {
    this._gameSocketService.addListener("init", (message) => {
      if (message === undefined) return;
      let gameState = JSON.parse(message);

      console.log("\n--------------");
      console.log("\nRECIEVING: init");
      console.log(gameState);

      this.game = this.gameFromState(gameState);
    });

    this._gameSocketService.addListener("updatemap", (message) => {
      console.log("\n--------------");
      console.log("\nRECIEVING: update map:");
      console.log(JSON.parse(message));
      this.updateMap(JSON.parse(message));
    });
  }

  endSelecting() {
    this._gameSocketService.send('cancel', '');
    this.game.isSelecting = false;
    this.game.toSelectStoppable = false;
    this.game.setNotSelecting();
  }

  cardClickedPile(card: Card) {
    if (this.game.isSelecting && this.game.isSelectable(card, false)) {
      this._gameSocketService.send('select', JSON.stringify({inhand: false, loc: card.id}));
      console.log("\n--------------");
      console.log("\nSENDING select:");
      console.log({inhand: false, loc: card.id});
      this.game.setNotSelecting();
    } else {
      this.addToCart(card);
    }
  }

  cardClickedHand(card: Card) {
    if (this.game.isSelecting && this.game.isSelectable(card, true)) {
      this._gameSocketService.send('select',
        JSON.stringify({inhand: true, loc: card.handPosition}));

      console.log("\n--------------");
      console.log("\nSENDING select:");
      console.log({inhand: true, loc: card.handPosition});
      this.game.setNotSelecting();
    } else {
      this.play(card);
    }
  }

  play(card: Card) {
    if (this.game.canPlay(card)) {
      console.log("\n--------------");
      console.log("\nSENDING 'doaction'");
      console.log({handid: card.handPosition});
      card.state = 'played';
      this.game.actions -= 1; // For Instantaneous disabling of cards
      setTimeout(() => {
        this._gameSocketService.send('doaction',
          JSON.stringify({handloc: card.handPosition}));
      }, 1000);
      // this.game.removeCardInHand(card);
    }
  }

  addToCart(card: Card) {
    if (this.game.phase === 'buy') {
      this.game.addToCart(card);
    }
  }

  endPhase() {
    if (this.game.phase === 'action') {
      console.log("\n--------------");
      console.log("\nSENDING 'endaction'");
      return this._gameSocketService.send('endaction', '');
    } else if (this.game.phase === 'buy') {
      console.log("\nSENDING 'endbuy'");
      let cart: number[] = [];
      this.game.cart.forEach(card => {
        cart.push(card.id);
      });
      this.game.cart = [];
      return this._gameSocketService.send('endbuy', JSON.stringify(cart));
    }
    throw "Phase is " + this.game.phase + ". Must be 'action' or 'buy'";
  }


  shouldDisplayCart() {
    return this.game.phase === 'buy' && this.game.cart.length !== 0;
  }

  gameFromState(state) {
    const players = state.users.map(player => {
      return new Player(player.id, player.color, player.name);
    });

    const cards = state.cardids.map(cardid => {
      return new Card(cardid);
    });

    return new ClientGame(players, this._userIdService.id, cards);
  }

  updateMap(update) {
    if (this.game != null) {
      if (typeof update.actions !== 'undefined') {
        this.game.actions = update.actions;
      }
      if (typeof update.buys !== "undefined") {
        this.game.buys = update.buys;
      }
      if (typeof update.gold !== "undefined") {
        this.game.gold = update.gold;
      }
      if (typeof update.phase !== "undefined") {
        this.game.phase = update.phase;

      }
      if (typeof update.select !== "undefined") {
        if (update.select) {
          this.game.isSelecting = true;
          this.game.toSelectHand = update.handSelect;
          this.game.toSelectBoard = update.boardSelect;
          this.game.toSelectStoppable = update.stoppable;
        } else {
          this.game.isSelecting = false;
        }
      }
      if (typeof update.hand !== "undefined") {

        let i = 0;
        this.game.hand = update.hand.map(cardid => {
          let card = new Card(cardid);
          card.handPosition = i++;
          return card;
        });
      }
      if (typeof update.decksize !== "undefined") {
        this.game.decksize = update.decksize;
      }
      if (typeof update.discardsize !== "undefined") {
        this.game.discardsize = update.discardsize;
      }
      if (typeof update.holding !== "undefined") {
        this.game.holding = update.holding;
      }
      if (typeof update.handcardnum !== 'undefined') {
        const player = this.game.getPlayerById(update.handcardnum.id);
        player.numCards = update.handcardnum.cards;
      }
      if (typeof update.turn !== "undefined") {
        this.game.setTurn(update.turn);
      }
      if (typeof update.board !== 'undefined') {
        this.game.updatePiles(update.board.piles);
      }
      if (typeof update.winner !== "undefined") {
        alert(update.winner[0].name + " won!");
        this.game.isOver = true;
      }
    }
    console.log("\n--------------");
    console.log("\nUPDATED GAME:");
    console.log(this.game);
    console.log(this.game.isSelecting);
  }

  chat(msg) {
    this.gameChat.addMessage(JSON.parse(msg));
  }
}
