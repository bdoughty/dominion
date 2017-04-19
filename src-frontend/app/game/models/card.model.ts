const cardInfo = {
  0: {
    name: 'Copper',
    text: '1 Money',
    cost: 0
  },
  1: {
    name: 'Silver',
    text: '2 Money',
    cost: 3
  },
  2: {
    name: 'Gold',
    text: '3 Money',
    cost: 6
  },
  3: {
    name: 'Estate',
    text: '1 Victory Point',
    cost: 2
  },
  4: {
    name: 'Dutchy',
    text: '3 Victory Points',
    cost: 5
  },
  5: {
    name: 'Province',
    text: '6 Victory Points',
    cost: 8
  },
  6: {
    name: 'Cellar',
    text: '',
    cost: 6
  },
  7: {
    name: 'Market',
    text: '',
    cost: 6
  },
  8: {
    name: 'Militia',
    text: '',
    cost: 6
  },
  9: {
    name: 'Mine',
    text: '',
    cost: 6
  },
  10: {
    name: 'Moat',
    text: '',
    cost: 6
  },
  11: {
    name: 'Remodel',
    text: '',
    cost: 6
  },
  12: {
    name: 'Gold',
    text: '',
    cost: 6
  },
  13: {
    name: 'Gold',
    text: '',
    cost: 6
  },
  14: {
    name: 'Gold',
    text: '',
    cost: 6
  },
  15: {
    name: 'Gold',
    text: '',
    cost: 6
  },
  16: {
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

