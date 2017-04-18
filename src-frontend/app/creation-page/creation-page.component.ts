import { Component } from '@angular/core';

@Component({
  selector: 'dmn-creation',
  templateUrl: './creation-page.component.html',
  styleUrls: ['./creation-page.component.css']
}) export class CreationComponent {
  title = 'app works!';

}

let blowUp = document.getElementById("blowUp");

function hoveringOver(cardId) {
  this.blowUp.innerHTML = "<img class='image' src='../../assets/card" + cardId + ".jpg' />";
}
