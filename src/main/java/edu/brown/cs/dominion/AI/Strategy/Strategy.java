package edu.brown.cs.dominion.AI.Strategy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

public interface Strategy {
  List<Integer> getDiscardPreferences(Player who);

  List<Integer> getBuyPreferences(Player who);

  int playAction(Player who);

  int trashForChapel(Player who);

  default int trashForMine(Player who) {
    List<Integer> hand = currentHand(who);
    if (hand.contains(0)) {
      return hand.indexOf(0);
    }
    if (hand.contains(1)
        && !who.getGame().getBoard().getPiles().get(1).isEmpty()) {
      return hand.indexOf(1);
    }

    return -1;
  }

  int trashForRemodel(Player who);

  int playThroneRoom(Player who);

  default int discard(Player who) {
    List<Integer> hand = currentHand(who);
    if (hand.size() > 0) {
      return who.getHand()
          .indexOf(Collections.min(hand,
              (one, two) -> Integer.compare(
                  getDiscardPreferences(who).indexOf(one),
                  getDiscardPreferences(who).indexOf(two))));
    }

    return -1;
  }

  default int buy(int money, Player who) {
    List<Integer> buyable = Strategy.buyable(money, who.getGame());
    if (buyable.size() > 0) {
      return Collections.min(buyable,
          (one, two) -> Integer.compare(getBuyPreferences(who).indexOf(one),
              getBuyPreferences(who).indexOf(two)));
    }

    return -1;
  }

  static List<Integer> buyable(int money, Game g) {
    return g.getBoard().getCardsUnderValue(money);
  }

  static List<Integer> currentHand(Player who) {
    return who.getHand().stream().map((card) -> card.getId())
        .collect(Collectors.toList());
  }
}
