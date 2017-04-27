package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Woodcutter extends AbstractAction {

  public Woodcutter() {
    super(15, 3);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.incrementBuys();
    g.incrementAdditionalMoney(2);
  }

  @Override
  public String toString() {
    return "Woodcutter";
  }

}
