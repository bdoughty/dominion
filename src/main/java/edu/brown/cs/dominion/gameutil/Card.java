package edu.brown.cs.dominion.gameutil;

import edu.brown.cs.dominion.action.ActionCenter;

public interface Card {

  void play(ActionCenter ac);

  int getCost();

  int getValue();

  int getPoints();

  int getId();

}
