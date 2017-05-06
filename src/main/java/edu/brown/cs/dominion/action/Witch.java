package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Witch extends AbstractAction {

  public Witch() {
    super(20, 5);
  }

  @Override
  public void play(Player p) {
    p.draw(2);
    p.getGame().othersGainCurse(p);
  }

  @Override
  public String toString() {
    return "Witch";
  }

}
