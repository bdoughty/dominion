import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameService} from "./game.service";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./models/card.model";

@Component({
  selector: 'dmn-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {
  public title = 'Dominion';
  public game;
  public gameChat = new Chat();
  public dummyCard;

  constructor(
    private _userIdService: UserIdService,
    private _gameService: GameService,
    private _gameSocketService: GameSocketService
  ) {}

  ngOnInit(): void {
    this.dummyCard = new Card(0);
    console.log(this.dummyCard);

    this._gameSocketService.addListener("init", (message) => {
      if (message === undefined) return;

      let gameState = JSON.parse(message);
      let game = this.gameFromState(gameState);
      this.game = game;


      // this.initGameFromState(initObj);
      // { gameid:int, users:[{id:int, name:string, color:String}], cardids:[int], }
    });

    this._gameSocketService.addListener("updatemap", (message) => {
      console.log(message);
      this.updateMap(JSON.parse(message));
    });

    this._gameSocketService.addListener("globalmap", (message) => {
      console.log("global update:" + message);
      this.globalMap(JSON.parse(message));
    })
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
        this.game.setTurn();
      }
      if (update.winner !== "undefined") {
        alert("")
      }
    }
  }

  updateMap(update) {
    if (this.game != null) {
      if (update.actions === "undefined") {
        this.game.actions = update.actions;
      }
      if (update.buys === "undefined") {
        this.game.buys = update.buys;
      }
      if (update.gold === "undefined") {
        this.game.gold = update.gold;
      }

      if (update.select === "undefined") {
        if (update.select) {
          this.game.toSelect = true;
          this.game.toSelectHand = update.handSelect;
          this.game.toSelectBoard = update.boardSelect;
        } else {
          this.game.toSelect = false;
        }
      }
      if (update.hand === "undefined") {
        this.game.hand = update.hand;
      }
      if (update.decksize === "undefined") {
        this.game.deck = update.decksize;
      }
      if (update.discardsize === "undefined") {
        this.game.discardPile = update.discardsize;
      }
      if (update.holding === "undefined") {
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
