package action;

import dominion.Card;

public abstract class AbstractAction implements Card {

  protected int cost;

  protected int id;

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
    return 0;
  }

  @Override
  public int getId() {
    return this.id;
  }

}
