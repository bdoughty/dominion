package edu.brown.cs.dominion.gameutil;

import java.util.function.Supplier;

import edu.brown.cs.dominion.Card;

public class Pile {

  private int size;
  private Supplier<Card> card;
  private int cost;

  public Pile(Supplier<Card> card, int size) {
    this.size = size;
    this.card = card;
    this.cost = this.card.get().getCost();
  }

  public Card draw() {
    if (!isEmpty()) {
      size--;
      return (card.get());
    } else {
      throw new UnsupportedOperationException("empty pile");
    }
  }

  public boolean isEmpty() {
    return (size == 0);
  }

  // ugly, but will suffice for now
  public int getCost() {
    return cost;
  }

}
