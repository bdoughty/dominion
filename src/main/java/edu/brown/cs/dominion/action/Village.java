package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Village extends AbstractAction {

  public Village() {
    super(14, 3);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.incrementActions();
    g.incrementActions();
    g.currentDraw(1);
  }

}
