package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;

public abstract class AbstractAction extends Card {

  protected int cost;

  protected int id;

  @Override
  public int getCost() {
    return this.cost;
  }

  @Override
  public int getMonetaryValue() {
    return 0;
  }

  @Override
  public int getVictoryPoints() {
    return 0;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
