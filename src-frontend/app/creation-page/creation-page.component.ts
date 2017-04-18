import { Component } from '@angular/core';

@Component({
  selector: 'dmn-creation',
  templateUrl: './creation-page.component.html',
  styleUrls: ['./creation-page.component.css']
}) export class CreationComponent {
  private selected = new Set();

  entered(cardId) {
    console.log("entered " + cardId);
    document.getElementById("blowUp").innerHTML = "<img id='blowUpImg' src='../../assets/" + cardId + ".jpg' " +
      "style='visibility: visible; width: auto; height: 100%;' />";
  }

  left() {
    console.log("left");
    document.getElementById("blowUpImg").style.visibility = "hidden";
  }

  clicked(cardId) {
    if(this.selected.has(cardId)) {
      this.selected.delete(cardId);
      document.getElementById(cardId).style.border = "0";
    } else {
      this.selected.add(cardId);
      document.getElementById(cardId).style.border = "3px solid yellow";
    }
  }

  submitForm() {

  }
}


