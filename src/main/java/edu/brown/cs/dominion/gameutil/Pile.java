package edu.brown.cs.dominion.gameutil;

import java.util.function.Supplier;

import edu.brown.cs.dominion.Card;

public class Pile {
  private int size;
  private transient Supplier<Card> card;
  private transient Card c;

  public Pile(Supplier<Card> card, int size) {
    this.size = size;
    this.card = card;
    this.c = card.get();
  }

  public Card draw() throws EmptyPileException {
    if (!isEmpty()) {
      size--;
      return (card.get());
    } else {
      throw new EmptyPileException("empty pile: " + card.toString());
    }
  }

  /**
   * Produce is for producing a card object that won't ever actually be played
   * in the game. This is for examining properties of the card (id, cost, etc.)
   * without putting it into anyone's hand, so we don't want to decrement the
   * pile size.
   *
   * @return card of interest
   */
  public Card produce() {
    return card.get();
  }

  public Card getCard() {
    return c;
  }

  public boolean isEmpty() {
    return (size == 0);
  }

  public int getCost() {
    return c.getCost();
  }

  public int getId() {
    return c.getId();
  }

}
