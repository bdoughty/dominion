package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class CouncilRoom extends AbstractAction {

  public CouncilRoom() {
    super(19, 5);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.currentDraw(4);
    g.incrementBuys();
    g.othersDraw(1);
  }

  @Override
  public String toString() {
    return "Council Room";
  }

}
