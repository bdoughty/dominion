package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(Game g) {
    g.incrementAdditionalMoney(2);
    // ac.militia();
  }

}
