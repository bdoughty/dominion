package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Village extends AbstractAction {

  public Village() {
    super(14, 3);
  }

  @Override
  public void play(Game g) {
    g.incrementActions();
    g.incrementActions();
    g.currentDraw(1);
  }

}
