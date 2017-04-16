package edu.brown.cs.dominion.gameutil;

public interface DeprecatedCard {

  void play(GameHandler ac);

  int getCost();

  int getMonetaryValue();

  int getVictoryPoints();

  int getId();

}
