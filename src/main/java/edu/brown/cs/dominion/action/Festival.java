package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Festival extends AbstractAction {

  public Festival() {
    super(17, 5);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.incrementActions();
    g.incrementActions();
    g.incrementBuys();
    g.incrementAdditionalMoney(2);
  }

  @Override
  public String toString() {
    return "Festival";
  }

}
