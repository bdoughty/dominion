import {Component, OnInit} from '@angular/core';
import {UserIdService} from "../shared/user-id.service";
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {Pile} from "./models/pile.model";
import {ClientGame} from "./models/client-game.model";
import {GameService} from "./game.service";
import {GameSocketService} from "../shared/gamesocket.service";

@Component({
  selector: 'dmn-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {
  public title = 'Dominion';
  public game = null;
  public gameChat = new Chat();

  constructor(
    private _userIdService: UserIdService,
    private _gameService: GameService,
    private _gameSocketService: GameSocketService
  ) {}

  ngOnInit(): void {
    let pile1 = new Pile(123, 3, 3);
    let player = new Player(4234, "name1", "#123123");
    this._gameSocketService.addListener("init", (m) => {
      alert(m);
    })
  }

  initGameFromState(state) {
    let gameStateFromServer = state;
    const players = gameStateFromServer.players;
    const playerMap = {};

    players.forEach(function (p) {
      playerMap[p.id] = new Player(p.id, p.color, p.name);
    });
    const turnList = players.map(function (p) {
      return p.id;
    });
    const cards = gameStateFromServer.cards;
    const pileArray = cards.map(function (row) {
      return row.map(function (p) {
        return new Pile(p.card.id, p.card.cost, p.count);
      })
    });

    this.game =
      new ClientGame(playerMap, pileArray, turnList, this._userIdService.id);
  }

  updateMap(updateMap) {
    const update = JSON.parse(updateMap);
    if (this.game != null) {
      if (update.actions === "undefined") {
        this.game.getSelf().actions = update.actions;
      }
      if (update.buys === "undefined") {
        this.game.getSelf().buys = update.buys;
      }
      if (update.gold === "undefined") {
        this.game.getSelf().gold = update.gold;
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
