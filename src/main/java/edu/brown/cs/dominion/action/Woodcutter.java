package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Woodcutter extends AbstractAction {

  public Woodcutter() {
    super(15, 3);
  }

  @Override
  public void play(GameHandler ac) {
    ac.incrementBuys();
    ac.incrementAdditionalMoney(2);
  }

}
