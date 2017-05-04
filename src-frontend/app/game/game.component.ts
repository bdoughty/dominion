import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./card/card.model";
import {trigger, state, style, animate, transition, keyframes} from "@angular/animations";
import {PlayerAction} from "./models/player-action.model";
import {Button} from "./models/button-interface";

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
  public notificationText: string = "";

  constructor(
    private _userIdService: UserIdService,
    private _gameSocketService: GameSocketService
  ) {}

  public ngOnInit(): void {
    this._gameSocketService.addListener("init", (message) => {
      if (message === undefined) return;
      let gameState = JSON.parse(message);
      this.game = this._gameFromState(gameState);
    });

    this._gameSocketService.addListener("updatemap", (message) => {
      this.updateMap(JSON.parse(message));
    });

    this._gameSocketService.addListener('redirect', (messageString) => {
      window.location.replace(messageString);
    });
  }

  public cardClickedPile(card: Card): void {
    if (this.game.isSelectable(card, false)) {
      this._gameSocketService.send('select', JSON.stringify({inhand: false, loc: card.id}));
    } else {
      this._addToCart(card);
    }
  }

  public cardClickedHand(card: Card): void {
    if (this.game.isSelectable(card, true)) {
      this._gameSocketService.send('select',
        JSON.stringify({inhand: true, loc: card.handPosition}));
    } else {
      this._play(card);
    }
  }

  public endPhaseClicked(): void {
    if (this.game.phase === 'action') {
      return this._gameSocketService.send('endaction', '');
    } else if (this.game.phase === 'buy') {
      let cart: number[] = [];
      this.game.cart.forEach(card => {
        cart.push(card.id);
      });
      this.game.cart = [];
      return this._gameSocketService.send('endbuy', JSON.stringify(cart));
    }
    throw "Phase is " + this.game.phase + ". Must be 'action' or 'buy'";
  }

  public shouldDisplayCart(): boolean {
    return this.game.phase === 'buy' && this.game.cart.length !== 0;
  }

  public customButtonClicked(id: string) {
    this.game.playerActionQueue.pop();
    this._gameSocketService.send('button', id);
  }

  public leaveGame() {
    this._gameSocketService.send('exit', '');
  }

  public chat(msg) {
    this.gameChat.addMessage(JSON.parse(msg));
  }



  /* ---------------------------- PRIVATE METHODS --------------------------- */


  private _addToCart(card: Card): void {
    if (this.game.phase === 'buy') {
      this.game.addToCart(card);
    }
  }

  private _notify(text: string) {
    this.notificationText = text;
  }

  private _play(card: Card): void {
    if (this.game.canPlay(card)) {
      card.state = 'played';
      this.game.actions -= 1; // For Instantaneous disabling of cards
      setTimeout(() => {
        this._gameSocketService.send('doaction',
          JSON.stringify({handloc: card.handPosition}));
      }, 1000);
      // this.game.removeCardInHand(card);
    }
  }

  private _gameFromState(state): ClientGame {
    const players = state.users.map(player => {
      return new Player(player.id, player.color, player.name);
    });

    const cards = state.cardids.map(cardid => {
      return new Card(cardid);
    });

    return new ClientGame(players, this._userIdService.id, cards);
  }

  private updateMap(update: any) {
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
        this.game.isOver = true;
      }
      if (typeof update.playeractions !== 'undefined') {
        update.playeractions.forEach(playerAction => {
          this.game.playerActionQueue.push(new PlayerAction(
            playerAction.urgent,
            playerAction.select,
            playerAction.handSelect,
            playerAction.boardSelect,
            playerAction.buttons
          ));
        });
      }
      if (typeof update.victorypoints !== 'undefined') {
        this.game.players.forEach(player => {
          player.victoryPoints = update.victorypoints[player.id];
        });
      }
      if (typeof update.notify !== 'undefined') {
        this._notify(update.notify);
      }
    }
    console.log("\n\n\n\nUPDATED GAME:");
    console.log(this.game);
  }

}
