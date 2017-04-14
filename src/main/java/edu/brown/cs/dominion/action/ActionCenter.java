package edu.brown.cs.dominion.action;

import java.util.Set;

import edu.brown.cs.dominion.gameutil.Player;

public class ActionCenter {

  private Player current;
  private Set<Player> others;

  public ActionCenter() {

  }

  public void incrementActions() {
    this.current.incrementActions();
  }

  public void incrementBuys() {
    this.current.incrementBuys();
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    this.current.incrementAdditionalMoney(adnlMoney);
  }

  public void currentDraw(int numCards) {
    this.current.draw(numCards);
  }

  public void militia() {
    for (Player p : others) {
      if (!p.hasMoat()) {

      }
    }

  }

}
