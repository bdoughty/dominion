package edu.brown.cs.dominion;

/**
 * Created by henry on 4/2/2017.
 */
public class Pile {
  private Card card;
  private int count;
  //all other variables should be transient to avoid lengthy GSON calls.

  public Pile(Card card, int count) {
    this.card = card;
    this.count = count;
  }

  public Pile(String cardName, int count) {
    this(Card.getCardFromName(cardName), count);
  }
}
