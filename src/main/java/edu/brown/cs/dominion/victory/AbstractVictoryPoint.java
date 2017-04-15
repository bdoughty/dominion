package edu.brown.cs.dominion.victory;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.ActionCenter;

public abstract class AbstractVictoryPoint extends Card {

  public AbstractVictoryPoint(int id, int cost, int victoryPoints) {
    super(id, cost, victoryPoints, 0);
  }

  @Override
  public void play(ActionCenter ac) {
    throw new UnsupportedOperationException("can't play victory points");
  }

}
