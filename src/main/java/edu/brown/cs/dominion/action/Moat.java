package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Moat extends AbstractAction {

  public Moat() {
    super(11, 2);
  }

  @Override
  public void play(GameHandler ac) {
    ac.currentDraw(2);
  }

}
