import { cardInfo } from "./card.data";

export class Card {
  public handPosition: number;
  public state: string = 'inhand';
  public pileCount: number;

  constructor(public id) {
  }

  get type() {
    if (cardInfo[this.id]) {
      return cardInfo[this.id].type;
    }
    return "action";
  }

  get name() {
    if (cardInfo[this.id]) {
      return cardInfo[this.id].name;
    }
    return "No Name";
  }

  get text() {
    if (cardInfo[this.id]) {
      return cardInfo[this.id].text;
    }
    return "Placeholder text";
  }

  get cost() {
    if (cardInfo[this.id]) {
      return cardInfo[this.id].cost;
    }
    return 0;
  }
}

