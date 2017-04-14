package edu.brown.cs.dominion.gameutil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.Cellar;
import edu.brown.cs.dominion.action.Market;
import edu.brown.cs.dominion.action.Militia;
import edu.brown.cs.dominion.action.Mine;
import edu.brown.cs.dominion.action.Moat;
import edu.brown.cs.dominion.action.Remodel;
import edu.brown.cs.dominion.action.Smithy;
import edu.brown.cs.dominion.action.Village;
import edu.brown.cs.dominion.action.Woodcutter;
import edu.brown.cs.dominion.action.Workshop;
import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.money.Gold;
import edu.brown.cs.dominion.money.Silver;
import edu.brown.cs.dominion.victory.Curse;
import edu.brown.cs.dominion.victory.Duchy;
import edu.brown.cs.dominion.victory.Estate;
import edu.brown.cs.dominion.victory.Province;

public class CardFactory {

  private static final CardFactory CF = new CardFactory();
  private Map<Integer, Supplier<Card>> cards;

  private CardFactory() {
    this.cards = new HashMap<Integer, Supplier<Card>>();
    // maybe I only want to put action cards in here?
    this.cards.put(0, Copper::new);
    this.cards.put(1, Silver::new);
    this.cards.put(2, Gold::new);
    this.cards.put(3, Estate::new);
    this.cards.put(4, Duchy::new);
    this.cards.put(5, Province::new);
    this.cards.put(6, Curse::new);
    this.cards.put(7, Cellar::new);
    this.cards.put(8, Market::new);
    this.cards.put(9, Militia::new);
    this.cards.put(10, Mine::new);
    this.cards.put(11, Moat::new);
    this.cards.put(12, Remodel::new);
    this.cards.put(13, Smithy::new);
    this.cards.put(14, Village::new);
    this.cards.put(15, Woodcutter::new);
    this.cards.put(16, Workshop::new);
  }

  public Supplier<Card> getFactory(int id) {
    if (this.cards.containsKey(id)) {
      return this.cards.get(id);
    } else {
      throw new IllegalArgumentException("id doesn't exist");
    }
  }

  public static CardFactory getInstance() {
    return CF;
  }
}
