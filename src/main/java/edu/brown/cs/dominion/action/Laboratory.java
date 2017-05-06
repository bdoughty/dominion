package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Laboratory extends AbstractAction {

  public Laboratory() {
    super(18, 5);
  }

  @Override
  public void play(Player p) {
    p.draw(2);
    p.incrementActions();
  }

  @Override
  public String toString() {
    return "Laboratory";
  }

}
