package edu.brown.cs.dominion.AI;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;

public interface Strategy {
  int playAction(Game g, User who);

  int discard(Game g, User who);

  int buy(int money, Game g, User who);
}
