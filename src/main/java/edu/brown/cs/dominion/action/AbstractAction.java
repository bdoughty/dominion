package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;

public abstract class AbstractAction extends Card {

  public AbstractAction(int id, int cost) {
    super(id, cost, 0, 0);
  }

}
