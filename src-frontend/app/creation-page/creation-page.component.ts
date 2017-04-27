import { Component } from '@angular/core';
import {ChatSocketService} from "../shared/chatsocket.service";

@Component({
  selector: 'dmn-creation',
  templateUrl: './creation-page.component.html',
  styleUrls: ['./creation-page.component.css']
}) export class CreationComponent{
  private selected = new Set();
  private allCards = new Set([7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26]);
  private basicPreset = new Set([7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);
  private moneyPreset = new Set([]);
  private trashPreset = new Set([]);

  constructor(private _chatSocketService: ChatSocketService) {
    _chatSocketService.addListener('redirect', (messageString) => {
      console.log(messageString);
      window.location.replace(messageString);
    });
  }

  entered(cardId) {
    document.getElementById("blowUp").innerHTML = "<img id='blowUpImg' src='../../assets/card/card" + cardId + ".jpg' " +
      "style='visibility: visible; width: auto; height: 100%;' />";
  }

  left() {
    document.getElementById("blowUpImg").style.visibility = "hidden";
  }

  clicked(cardId) {
    if(this.selected.has(cardId)) {
      this.selected.delete(cardId);
      document.getElementById("card" + cardId).style.border = "0";
    } else {
      this.selected.add(cardId);
      document.getElementById("card" + cardId).style.border = "3px solid yellow";
    }
  }

  validate() {
    if(this.selected.size == 10) {
      if((<HTMLInputElement>document.getElementById("gameName")).value != "") {
        let out = [];
        this.selected.forEach((car) => {out.push(car)});
        console.log(out);
        const toSend = JSON.stringify({
          name: (<HTMLInputElement>document.getElementById("gameName")).value,
          numPlayers: (<HTMLInputElement>document.getElementById("numPlayers")).value, cards: out});
        console.log(toSend);
        this._chatSocketService.send("create", toSend);
      } else {
        alert("You must supply a game name to begin a game!");
      }
    } else {
      alert("You must select exactly 10 cards to begin a game!");
    }
  }

  highlightPreset(toHighlight: Set<number>) {
    console.log("toHighlight: " + toHighlight);
    this.selected.clear();
    this.allCards.forEach((i) => {
      console.log(i);
      if(toHighlight.has(i)) {
        this.selected.add(i);
        document.getElementById("card" + i).style.border = "3px solid yellow";
      } else {
        document.getElementById("card" + i).style.border = "0";
      }
    });
    console.log("selected: " + this.selected);
  }

  basic() {
    this.highlightPreset(this.basicPreset);
  }

  money() {
    this.highlightPreset(this.moneyPreset);
  }

  randomSelect() {
    this.highlightPreset(this.getRandomTen(this.allCards));
  }

  getRandomTen(selectFrom: Set<number>) {
    let from = [];
    selectFrom.forEach((f) => {from.push(f);});
    let gotten = new Set();
    while(gotten.size < 10) {
      let toAdd = from[Math.floor(Math.random() * from.length)];
      while(gotten.has(toAdd)) {
        toAdd = from[Math.floor(Math.random() * from.length)];
      }
      gotten.add(toAdd);
    }
    return gotten;
  }

  trash() {
    this.highlightPreset(this.trashPreset);
  }
}


