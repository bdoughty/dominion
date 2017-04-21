import {Component, Input} from "@angular/core";
import {Card} from "./card.model";

@Component({
  selector: 'dmn-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css'],
})
export class CardComponent {
  @Input() public card: Card;
  @Input() public count: number;
  public selected: boolean;
  public state = 'inhand';

  constructor() {}

  hasCount() {
    return typeof this.count !== 'undefined';
  }

  imageSource() {
    if (this.card) {
      // return "../../../assets/cardimage" + this.card.id + ".jpg";
      return "url('../../../assets/cardimage0.jpg')";
    } else {
      return "../../../assets/cardback.jpg";
    }
  }

  play() {
  }

  select() {
    this.state = 'played';
    this.selected = !this.selected;
  }
}
