package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Moat extends AbstractAction {

  public Moat() {
    super(11, 2);
  }

  @Override
  public void play(Player p) {
    p.draw(2);
  }

  @Override
  public String toString() {
    return "Moat";
  }

}
