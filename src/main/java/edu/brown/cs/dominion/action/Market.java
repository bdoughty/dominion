package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.players.Player;

public class Market extends AbstractAction {

  public Market() {
    super(8, 5);
  }

  @Override
  public void play(Player p) {
    p.draw(1);
    p.incrementActions();
    p.incrementBuys();
    p.incrementAdditionalMoney(1);
  }

  @Override
  public String toString() {
    return "Market";
  }
}
