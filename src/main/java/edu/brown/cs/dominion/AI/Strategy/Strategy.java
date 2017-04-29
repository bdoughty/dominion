package edu.brown.cs.dominion.AI.Strategy;

import java.util.List;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;

public interface Strategy {
  int playAction(Game g, User who);

  int discard(Game g, User who);

  int buy(int money, Game g, User who);

  static List<Integer> buyable(int money, Game g) {
    return g.getBoard().getCardUnderValue(money);
  }
}
