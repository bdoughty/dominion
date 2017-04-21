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
  @Input() public disabled: boolean = false;
  public selectable: boolean = false;
  public selected: boolean;

  constructor() {}

  hasCount() {
    return typeof this.count !== 'undefined';
  }

  imageSource() {
    if (this.card) {
      // return "../../../assets/cardimage" + this.card.id + ".jpg";
      return "url('../../../assets/cardimage/card" + this.card.id + ".jpg')";
    } else {
      return "../../../assets/cardback.jpg";
    }
  }

  type() {
    if (this.hasCount()) {
      return "";
    } else {
      return this.card.type;
    }
  }

  select() {
    this.selected = !this.selected;
  }
}
