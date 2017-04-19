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
    name: 'Curse',
    text: '-1 Victory Point',
    cost: 0
  },
  7: {
    name: 'Cellar',
    text: '+1 action <br/> Discard any number of cards. </br> +1 Card per' +
    ' each card discarded',
    cost: 2
  },
  8: {
    name: 'Market',
    text: '+1 Card <br/> +1 Action <br/> +1 Buy <br/> +1 Money',
    cost: 5
  },
  9: {
    name: 'Militia',
    text: '',
    cost: 6
  },
  10: {
    name: 'Mine',
    text: '',
    cost: 6
  },
  11: {
    name: 'Moat',
    text: '',
    cost: 6
  },
  12: {
    name: 'Remodel',
    text: '',
    cost: 6
  },
  13: {
    name: 'Smithy',
    text: '',
    cost: 6
  },
  14: {
    name: 'Village',
    text: '',
    cost: 6
  },
  15: {
    name: 'Woodcutter',
    text: '',
    cost: 6
  },
  16: {
    name: 'Workshop',
    text: '',
    cost: 6
  }
};

export class Card {
  constructor(public id) {
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

