import {Component, Input} from "@angular/core";
import {Card} from "../models/card.model";

@Component({
  selector: 'dmn-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  @Input() public card: Card;
  @Input() public count: number;

  constructor() {}

  hasCount() {
    return typeof this.count !== 'undefined';
  }

  imageSource() {
    if (this.card) {
      return "../../../assets/cardimage" + this.card.id + ".jpg";
    } else {
      return "../../../assets/cardback.jpg";
    }
  }
}
