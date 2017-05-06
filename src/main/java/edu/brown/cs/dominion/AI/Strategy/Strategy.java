package edu.brown.cs.dominion.AI.Strategy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

public interface Strategy {
  List<Integer> getDiscardPreferences();

  List<Integer> getBuyPreferences();

  int playAction(Game g, Player who);

  default int discard(Game g, Player who) {
    List<Integer> hand = who.getHand().stream().map((card) -> card.getId())
        .collect(Collectors.toList());
    if (hand.size() > 0) {
      return who.getHand()
          .indexOf(Collections.min(hand,
              (one, two) -> Integer.compare(
                  getDiscardPreferences().indexOf(one),
                  getDiscardPreferences().indexOf(two))));
    }

    return -1;
  }

  default int buy(int money, Game g, Player who) {
    List<Integer> buyable = Strategy.buyable(money, g);
    if (buyable.size() > 0) {
      return Collections.min(buyable,
          (one, two) -> Integer.compare(getBuyPreferences().indexOf(one),
              getBuyPreferences().indexOf(two)));
    }

    return -1;
  }

  static List<Integer> buyable(int money, Game g) {
    return g.getBoard().getCardsUnderValue(money);
  }
}
