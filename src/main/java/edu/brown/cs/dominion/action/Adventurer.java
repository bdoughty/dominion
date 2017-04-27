package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Adventurer extends AbstractAction {

  public Adventurer() {
    super(21, 6);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.getCurrentPlayer().adventurer();
    g.playerUpdateMap(cm, g.getCurrentPlayer());
  }

  @Override
  public String toString() {
    return "Adventurer";
  }
}
