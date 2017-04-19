package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Moat extends AbstractAction {

  public Moat() {
    super(11, 2);
  }

  @Override
  public void play(Game g) {
    g.currentDraw(2);
  }

}
