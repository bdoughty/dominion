package edu.brown.cs.dominion.money;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.ActionCenter;

public abstract class AbstractMoney extends Card {

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
  public int getMonetaryValue() {
    return this.value;
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
