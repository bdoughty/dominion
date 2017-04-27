package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Smithy extends AbstractAction {

  public Smithy() {
    super(13, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.currentDraw(3);
  }

  @Override
  public String toString() {
    return "Smithy";
  }

}
