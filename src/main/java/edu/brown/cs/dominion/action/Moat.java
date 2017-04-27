package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Moat extends AbstractAction {

  public Moat() {
    super(11, 2);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.currentDraw(2);
  }

  @Override
  public String toString() {
    return "Moat";
  }

}
