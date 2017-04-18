export class Card {
  constructor(public id) {
  }

  get text() {
    return "+1 Card<br/>+2 Action";
  }

  get cost() {
    return 2;
  }
}
