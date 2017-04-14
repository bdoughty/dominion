import {Injectable} from '@angular/core';
import {Chat} from "../game/models/chat.model";
import {ClientGame} from "../game/models/client-game.model";
import {Pile} from "../game/models/pile.model";
import {Player} from "../game/models/player.model";
import {UserIdService} from "./user-id.service";


@Injectable()
export class MessageService {
  constructor(private _userIdService: UserIdService) {}
  private game = null;
  private gameChat = null;

  initGame(gameInitString) {
    const gameStateFromServer = JSON.parse(gameInitString);
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

    this.game = new ClientGame(playerMap, pileArray, turnList, this._userIdService.id);
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

