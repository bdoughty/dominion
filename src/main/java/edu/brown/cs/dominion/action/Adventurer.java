package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Adventurer extends AbstractAction {

  public Adventurer() {
    super(21, 6);
  }

  @Override
  public void play(Player p) {
    p.adventurer();
  }

  @Override
  public String toString() {
    return "Adventurer";
  }
}
