package dominion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import action.Cellar;
import action.Market;
import action.Militia;
import action.Mine;
import action.Moat;
import action.Remodel;
import action.Smithy;
import action.Village;
import action.Woodcutter;
import action.Workshop;
import money.Copper;
import money.Gold;
import money.Silver;
import victory.Curse;
import victory.Duchy;
import victory.Estate;
import victory.Province;

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
