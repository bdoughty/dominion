package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Market extends AbstractAction {

  public Market() {
    super(8, 5);
  }

  @Override
  public void play(Game g) {
    g.currentDraw(1);
    g.incrementActions();
    g.incrementBuys();
    g.incrementAdditionalMoney(1);
  }

}
