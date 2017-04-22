import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameService} from "./game.service";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./card/card.model";
import {trigger, state, style, animate, transition, keyframes} from "@angular/animations";

@Component({
  selector: 'dmn-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
  animations: [
    trigger('cardState', [
      state('played', style({
        display: 'none'
      })),
      transition('inhand => played', [
        animate('1000ms ease', keyframes([
          style({position: 'relative', top: '0', offset: 0}),
          style({top: '-500px', position: 'relative', opacity: '0', offset: 0.5}),
          style({width: '0', offset: 1})
        ]))
      ])
    ])
  ]
})
export class GameComponent implements OnInit {
  public title = 'Dominion';
  public game;
  public gameChat = new Chat();

  constructor(
    private _userIdService: UserIdService,
    private _gameService: GameService,
    private _gameSocketService: GameSocketService
  ) {}

  ngOnInit(): void {
    this._gameSocketService.addListener("init", (message) => {
      if (message === undefined) return;
      let gameState = JSON.parse(message);

      console.log("init map:");
      console.log(gameState);

      this.game = this.gameFromState(gameState);
    });

    this._gameSocketService.addListener("updatemap", (message) => {
      console.log("update map:");
      console.log(message);
      this.updateMap(JSON.parse(message));
    });

    this._gameSocketService.addListener("globalmap", (message) => {
      console.log("global update:" + message);
      this.globalMap(JSON.parse(message));
    })
  }

  play(card: Card) {
    if (this.game.isOwnTurn() && this.game.phase === "action"
        && (card.type == 'action' || card.type == 'reaction')) {

      this._gameSocketService.send('doaction',
        JSON.stringify({handloc: card.handPosition}));
      console.log("SENDING 'doaction'");
      console.log(JSON.stringify({handid: card.handPosition}));
      card.state = 'played';
      // this.game.removeCardInHand(card);
    }
  }

  endPhase() {
    if (this.game.phase === 'action') {
      console.log("SENDING 'endaction'");
      return this._gameSocketService.send('endaction', '');
    } else if (this.game.phase === 'buy') {
      console.log("SENDING 'endbuy'");
      let cart: number[] = [];
      this.game.cart.forEach(card => {
        cart.push(card.id);
      });
      return this._gameSocketService.send('endbuy', JSON.stringify(cart));
    }
    throw "Phase is " + this.game.phase + ". Must be 'action' or 'buy'";
  }

  addToCart(card: Card) {
    if (this.game.phase === 'buy') {
      this.game.addToCart(card.id);
    }
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

  globalMap(update) {
    if (this.game != null) {
      if (update.turn !== "undefined") {
        this.game.setTurn(update.turn);
      }
      if (update.winner !== "undefined") {
        alert("")
      }
    }
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
          this.game.toSelect = true;
          this.game.toSelectHand = update.handSelect;
          this.game.toSelectBoard = update.boardSelect;
        } else {
          this.game.toSelect = false;
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
    }
  }

  chat(msg) {
    this.gameChat.addMessage(JSON.parse(msg));
  }
}
