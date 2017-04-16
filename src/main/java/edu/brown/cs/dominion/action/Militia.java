package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(GameHandler ac) {
    ac.incrementAdditionalMoney(2);
    ac.militia();
  }

}