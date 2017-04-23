package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Market extends AbstractAction {

  public Market() {
    super(8, 5);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.currentDraw(1);
    g.incrementActions();
    g.incrementBuys();
    g.incrementAdditionalMoney(1);
  }

}
