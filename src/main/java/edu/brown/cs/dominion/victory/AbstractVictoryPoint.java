package edu.brown.cs.dominion.victory;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.ActionCenter;

public abstract class AbstractVictoryPoint extends Card {

  protected int cost;

  protected int points;

  protected int id;

  @Override
  public void play(ActionCenter ac) {
    throw new UnsupportedOperationException("can't play victory points");
  }

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
    return this.points;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
