import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameService} from "./game.service";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./card/card.model";
import {trigger, state, style, animate, transition} from "@angular/animations";

@Component({
  selector: 'dmn-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
  animations: [
    trigger('cardState', [
      state('inhand', style({
        position: 'relative',
        top: '0'
      })),
      transition('* => void', [
        animate('1000ms ease', style({
          top: '-500px',
          opacity: '0',
          'max-width': '0'
        }))
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
      let game = this.gameFromState(gameState);
      this.game = game;
    });

    this._gameSocketService.addListener("updatemap", (message) => {
      console.log("update map:")
      console.log(message);
      this.updateMap(JSON.parse(message));
    });

    this._gameSocketService.addListener("globalmap", (message) => {
      console.log("global update:" + message);
      this.globalMap(JSON.parse(message));
    })
  }

  play(card: Card) {
    this.game.removeCardInHand(card);
  }

  gameFromState(state) {
    const players = state.users.map(player => {
      return new Player(player.id, player.color, player.name);
    });

    const cards = state.cardids.map(cardid => {
      new Card(cardid);
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
        this.game.hand = update.hand.map(cardid => {return new Card(cardid)});
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

  stophold(msg) {
    this.game.holding = false;
  }

  chat(msg) {
    this.gameChat.addMessage(JSON.parse(msg));
  }
}
