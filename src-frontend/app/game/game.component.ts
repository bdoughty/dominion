import {Component, OnInit} from '@angular/core';
import {Chat} from "../chat/chat.model";
import {Player} from "./models/player.model";
import {ClientGame} from "./models/client-game.model";
import {GameSocketService} from "../shared/gamesocket.service";
import {Card} from "./card/card.model";
import {trigger, state, style, animate, transition, keyframes} from "@angular/animations";
import {PlayerAction} from "./models/player-action.model";

const MAX_TIME = 60000; // ms

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
    // ,trigger('timerState', [
    //   state('start', style({width: '100%'})),
    //   state('end', style({width: '0%'})),
    //   transition('start => end', animate(TURN_TIME + 's linear'))
    // ])
  ]
})
export class GameComponent implements OnInit {
  public title = 'Dominion';
  public game: ClientGame;
  public gameChat = new Chat();
  public notificationText: string = "";

  private _currTime;
  private _timerInterval;
  private _firstTimer = true;

  constructor(
    private _gameSocketService: GameSocketService
  ) {}

  public ngOnInit(): void {
    this._addListeners();
  }

  public cardClickedPile(card: Card): void {
    console.log(this.game);

    if (this.game.isSelectable(card, false)) {
      const playerAction = this.game.playerActionQueue.shift();
      this._gameSocketService.send('select',
        JSON.stringify({
          inhand: false,
          loc: card.id,
          id: playerAction.id
        }));
    } else {
      this._addToCart(card);
    }
  }

  public cardClickedHand(card: Card): void {
    if (this.game.isSelectable(card, true)) {
      const playerAction = this.game.playerActionQueue.shift();

      this._gameSocketService.send('select',
        JSON.stringify({
          inhand: true,
          loc: card.handPosition,
          id: playerAction.id
        }));
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
    this.game.playerActionQueue.shift();
    this._gameSocketService.send('button', id);
  }

  public leaveGame() {
    this._gameSocketService.send('exit', '');
  }

  public chat(msg) {
    this.gameChat.addMessage(JSON.parse(msg));
  }

  public cancel() {
    this._gameSocketService.send('cancel',
      this.game.playerActionQueue.shift().id + '');
  }

  public get timerPercent() {
    return this._currTime / MAX_TIME;
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

    return new ClientGame(players, state.id, cards);
  }

  private _addListeners() {

    this._gameSocketService.addListener("init", (message) => {
      if (message === undefined) return;
      let gameState = JSON.parse(message);
      this.game = this._gameFromState(gameState);

      this._currTime = gameState.time;
    });

    this._gameSocketService.addListener('redirect', (messageString) => {
      window.location.replace(messageString);
    });

    this._gameSocketService.addListener('actions', (message) => {
      if(this.game != null) {
        this.game.actions = parseInt(message);
      }
    });

    this._gameSocketService.addListener('buys', (message) => {
      if(this.game != null) {
        this.game.buys = parseInt(message);
      }
    });

    this._gameSocketService.addListener('gold', (message) => {
      if(this.game != null) {
        this.game.gold = parseInt(message);
      }
    });

    this._gameSocketService.addListener('phase', (message) => {
      if(this.game != null) {
        this.game.phase = message;
      }
    });

    this._gameSocketService.addListener('hand', (message) => {
      if(this.game != null) {
        let i = 0;
        let hand = JSON.parse(message);
        this.game.hand = hand.map(cardid => {
          let card = new Card(cardid);
          card.handPosition = i++;
          return card;
        });
      }
    });

    this._gameSocketService.addListener('decksize', (message) => {
      this.game.decksize = parseInt(message);
    });

    this._gameSocketService.addListener('discardsize', (message) => {
      this.game.discardsize = parseInt(message);
    });

    this._gameSocketService.addListener('handcardnum', (message) => {
      const update = JSON.parse(message);
      const player = this.game.getPlayerById(update.id);
      player.numCards = update.cards;
    });

    this._gameSocketService.addListener('turn', (message) => {
      this.game.setTurn(parseInt(message));

      if (!this._firstTimer) {
        this._currTime = MAX_TIME;
      }
      this._firstTimer = false;

      const updateRate = 10; // Reset timer after every turn
      clearInterval(this._timerInterval);
      this._timerInterval = setInterval(() => {
        this._currTime -= updateRate;
        if (this._currTime < 0) {
          clearInterval(this._timerInterval);
        }
      }, updateRate);
    });

    this._gameSocketService.addListener('board', (message) => {
      this.game.updatePiles(JSON.parse(message).piles);
    });

    this._gameSocketService.addListener('winner', () => {
      this.game.isOver = true;
    });

    this._gameSocketService.addListener('playeractions', (message) => {
      JSON.parse(message).forEach(playerAction => {
        this.game.playerActionQueue.push(new PlayerAction(
          playerAction.urgent,
          playerAction.select,
          playerAction.handselect,
          playerAction.boardselect,
          playerAction.buttons,
          playerAction.cancel,
          playerAction.id
        ));
      });
    });

    this._gameSocketService.addListener('victorypoints', (message) => {
      const victorypoints = JSON.parse(message);
      this.game.players.forEach(player => {
        player.victoryPoints = victorypoints[player.id];
      });
    });

    this._gameSocketService.addListener('notify', (message) => {
      this._notify(message);
    });


    this._gameSocketService.addListener('removeplayer', (playerId) => {
      this.game.removePlayer(parseInt(playerId));
    });
  }
}
