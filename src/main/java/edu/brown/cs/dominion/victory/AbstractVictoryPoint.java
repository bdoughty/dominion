package edu.brown.cs.dominion.victory;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public abstract class AbstractVictoryPoint extends Card {

  public AbstractVictoryPoint(int id, int cost, int victoryPoints) {
    super(id, cost, victoryPoints, 0);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) throws NotActionException {
    throw new NotActionException("can't play victory points");
  }

}
