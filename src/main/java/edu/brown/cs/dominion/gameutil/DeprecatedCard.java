package edu.brown.cs.dominion.gameutil;

public interface DeprecatedCard {

  void play(DeprecatedGameHandler ac);

  int getCost();

  int getMonetaryValue();

  int getVictoryPoints();

  int getId();

}
