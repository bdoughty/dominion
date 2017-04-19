package edu.brown.cs.dominion.gameutil;

@Deprecated
public interface DeprecatedCard {

  void play(DeprecatedGameHandler ac);

  int getCost();

  int getMonetaryValue();

  int getVictoryPoints();

  int getId();

}
