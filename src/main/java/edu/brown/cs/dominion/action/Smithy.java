package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.GameHandler;

public class Smithy extends AbstractAction {

  public Smithy() {
    super(13, 4);
  }

  @Override
  public void play(GameHandler ac) {
    ac.currentDraw(3);
  }

}
