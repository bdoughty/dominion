package edu.brown.cs.dominion.money;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.GameHandler;

public abstract class AbstractMoney extends Card {

  public AbstractMoney(int id, int cost, int monetaryValue) {
    super(id, cost, 0, monetaryValue);
  }

  @Override
  public void play(GameHandler ac) {
    throw new UnsupportedOperationException("can't play money");
  }

}