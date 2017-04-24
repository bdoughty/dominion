import { Component} from '@angular/core';
import {ChatSocketService} from "../shared/chatsocket.service";

@Component({
  selector: 'dmn-creation',
  templateUrl: './creation-page.component.html',
  styleUrls: ['./creation-page.component.css']
}) export class CreationComponent{
  private selected = new Set();

  constructor(private _chatSocketService: ChatSocketService) {
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
        this._chatSocketService.send("create", JSON.stringify({
            name: (<HTMLInputElement>document.getElementById("gameName")).value,
            numPlayers: (<HTMLInputElement>document.getElementById("numPlayers")).value, cards: this.selected}));
      } else {
        alert("You must supply a game name to begin a game!");
      }
    } else {
      alert("You must select exactly 10 cards to begin a game!");
    }
  }
}


