package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Woodcutter extends AbstractAction {

  public Woodcutter() {
    super(15, 3);
  }

  @Override
  public void play(Game g) {
    g.incrementBuys();
    g.incrementAdditionalMoney(2);
  }

}
