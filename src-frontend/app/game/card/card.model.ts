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
    text: '+1 action, Discard any number of cards. <br/> +1 Card per' +
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
    text: '+2 Money <br/> Each other player discards down to 3 cards in his hand',
    cost: 6
  },
  10: {
    name: 'Mine',
    text: 'Trash a Treasure card from your hand. Gain a Treasure card costing' +
    ' up to 3 more; put it into your hand.',
    cost: 6
  },
  11: {
    name: 'Moat',
    text: '+2 Cards. When another player plays an Attack card, you may reveal' +
    ' this from your hand to become unaffected.',
    cost: 6
  },
  12: {
    name: 'Remodel',
    text: 'Trash a card from your hand. Gain a card costing up to 2 more than' +
    ' the trashed card.',
    cost: 6
  },
  13: {
    name: 'Smithy',
    text: '+3 Cards',
    cost: 6
  },
  14: {
    name: 'Village',
    text: '+1 Cards<br/>+2 Actions',
    cost: 6
  },
  15: {
    name: 'Woodcutter',
    text: '+1 Buy<br/>+2 Money',
    cost: 6
  },
  16: {
    name: 'Workshop',
    text: 'Gain a card costing up to 4 Money',
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

