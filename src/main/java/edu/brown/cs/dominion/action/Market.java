package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Market extends AbstractAction {

  public Market() {
    super(8, 5);
  }

  @Override
  public void play(GameHandler ac) {
    ac.currentDraw(1);
    ac.incrementActions();
    ac.incrementBuys();
    ac.incrementAdditionalMoney(1);
  }

}
