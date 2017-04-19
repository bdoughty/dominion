const cardInfo = {
  0: {
    name: 'Copper',
    text: '',
    cost: 0
  },
  1: {
    name: 'Silver',
    text: '',
    cost: 3
  },
  2: {
    name: 'Gold',
    text: '',
    cost: 6
  }
};

export class Card {
  constructor(public id) {
  }

  get name() {
    return "Village";
  }

  get text() {
    return "+1 Card<br/>+2 Action";
  }

  get cost() {
    return 2;
  }
}

