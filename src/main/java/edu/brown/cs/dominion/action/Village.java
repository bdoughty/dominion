package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Village extends AbstractAction {

  public Village() {
    super(14, 3);
  }

  @Override
  public void play(GameHandler ac) {
    ac.incrementActions();
    ac.incrementActions();
    ac.currentDraw(1);
  }

}
