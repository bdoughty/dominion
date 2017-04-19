package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Smithy extends AbstractAction {

  public Smithy() {
    super(13, 4);
  }

  @Override
  public void play(Game g) {
    g.currentDraw(3);
  }

}
