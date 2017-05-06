package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.players.Player;

public class Festival extends AbstractAction {

  public Festival() {
    super(17, 5);
  }

  @Override
  public void play(Player p) {
    p.incrementActions();
    p.incrementActions();
    p.incrementBuys();
    p.incrementAdditionalMoney(2);
  }

  @Override
  public String toString() {
    return "Festival";
  }

}
