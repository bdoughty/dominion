const cardInfo = {
  0: {
    name: 'Copper',
    text: '1 Money',
    type: 'money',
    cost: 0
  },
  1: {
    name: 'Silver',
    text: '2 Money',
    type: 'money',
    cost: 3
  },
  2: {
    name: 'Gold',
    text: '3 Money',
    type: 'money',
    cost: 6
  },
  3: {
    name: 'Estate',
    text: '1 Victory Point',
    type: 'victory',
    cost: 2
  },
  4: {
    name: 'Dutchy',
    text: '3 Victory Points',
    type: 'victory',
    cost: 5
  },
  5: {
    name: 'Province',
    text: '6 Victory Points',
    type: 'victory',
    cost: 8
  },
  6: {
    name: 'Curse',
    text: '-1 Victory Point',
    type: 'action',
    cost: 0
  },
  7: {
    name: 'Cellar',
    text: '+1 action, Discard any number of cards. <br/> +1 Card per' +
    ' each card discarded',
    type: 'action',
    cost: 2
  },
  8: {
    name: 'Market',
    text: '+1 Card <br/> +1 Action <br/> +1 Buy <br/> +1 Money',
    type: 'action',
    cost: 5
  },
  9: {
    name: 'Militia',
    text: '+2 Money <br/> Each other player discards down to 3 cards in his hand',
    type: 'action',
    cost: 4
  },
  10: {
    name: 'Mine',
    text: 'Trash a Treasure card from your hand. Gain a Treasure card costing' +
    ' up to 3 more; put it into your hand.',
    type: 'action',
    cost: 5
  },
  11: {
    name: 'Moat',
    text: '+2 Cards. When another player plays an Attack card, you may reveal' +
    ' this from your hand to become unaffected.',
    type: 'reaction',
    cost: 2
  },
  12: {
    name: 'Remodel',
    text: 'Trash a card from your hand. Gain a card costing up to 2 more than' +
    ' the trashed card.',
    type: 'action',
    cost: 4
  },
  13: {
    name: 'Smithy',
    text: '+3 Cards',
    type: 'action',
    cost: 4
  },
  14: {
    name: 'Village',
    text: '+1 Cards<br/>+2 Actions',
    type: 'action',
    cost: 3
  },
  15: {
    name: 'Woodcutter',
    text: '+1 Buy<br/>+2 Money',
    type: 'action',
    cost: 3
  },
  16: {
    name: 'Workshop',
    text: 'Gain a card costing up to 4 Money',
    type: 'action',
    cost: 3
  }
};

export class Card {
  public handPosition: number;
  public state: string = 'inhand';

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

