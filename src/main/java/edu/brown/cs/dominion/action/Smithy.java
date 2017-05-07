package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Smithy extends AbstractAction {

  public Smithy() {
    super(13, 4);
  }

  @Override
  public void play(Player p) {
    p.draw(3);
  }

  @Override
  public String toString() {
    return "Smithy";
  }

}
