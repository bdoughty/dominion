package edu.brown.cs.dominion.gameutil;

import java.util.function.Supplier;

import edu.brown.cs.dominion.Card;

public class Pile {

  private int size;
  private Supplier<Card> card;

  public Pile(Supplier<Card> card, int size) {
    this.size = size;
    this.card = card;
  }

  public Card draw() {
    if (!isEmpty()) {
      size--;
      return card.get();
    } else {
      throw new UnsupportedOperationException("empty pile");
    }
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  // ugly, but will suffice for noe
  public int getCost() {
    return this.card.get().getCost();
  }

}
