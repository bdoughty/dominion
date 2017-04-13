package edu.brown.cs.dominion.money;

import edu.brown.cs.dominion.action.ActionCenter;
import edu.brown.cs.dominion.gameutil.Card;

public abstract class AbstractMoney implements Card {

  protected int cost;

  protected int value;

  protected int id;

  @Override
  public void play(ActionCenter ac) {
    throw new UnsupportedOperationException("can't play money");
  }

  @Override
  public int getCost() {
    return this.cost;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public int getPoints() {
    return 0;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
