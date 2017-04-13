package edu.brown.cs.dominion.victory;

import edu.brown.cs.dominion.action.ActionCenter;
import edu.brown.cs.dominion.gameutil.Card;

public abstract class AbstractVictoryPoint implements Card {

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
  public int getValue() {
    return 0;
  }

  @Override
  public int getPoints() {
    return this.points;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
