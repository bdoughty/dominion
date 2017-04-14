package edu.brown.cs.dominion.gameutil;

import edu.brown.cs.dominion.action.ActionCenter;

public interface DeprecatedCard {

  void play(ActionCenter ac);

  int getCost();

  int getMonetaryValue();

  int getVictoryPoints();

  int getId();

}
