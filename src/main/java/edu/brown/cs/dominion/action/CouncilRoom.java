package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class CouncilRoom extends AbstractAction {

  public CouncilRoom() {
    super(19, 5);
  }

  @Override
  public void play(Player p) {
    p.draw(4);
    p.incrementBuys();
    p.getGame().othersDraw(p, 1);
  }

  @Override
  public String toString() {
    return "Council Room";
  }

}
