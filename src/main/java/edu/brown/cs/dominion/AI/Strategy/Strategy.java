package edu.brown.cs.dominion.AI.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.Button;
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
    if (hand.contains(1)) {
      return hand.indexOf(1);
    }

    return hand.indexOf(2);
  }

  int trashForRemodel(Player who);

  int playThroneRoom(Player who);

  default int discard(Player who) {
    List<Integer> hand = currentHand(who);
    if (hand.size() > 0) {
      return hand.indexOf(Collections.min(hand,
          (one, two) -> Integer.compare(getDiscardPreferences(who).indexOf(one),
              getDiscardPreferences(who).indexOf(two))));
    }

    return -1;
  }

  default List<Integer> buy(int money, Player who) {
    List<Integer> buyable = Strategy.buyable(money, who.getGame());
    buyable.retainAll(getBuyPreferences(who));
    if (buyable.size() > 0) {
      Collections.sort(buyable,
          (one, two) -> Integer.compare(getBuyPreferences(who).indexOf(one),
              getBuyPreferences(who).indexOf(two)));
    }

    return buyable;
  }

  default int gain(List<Integer> cardIds, Player who) {
    List<Integer> gainable = new ArrayList<>(cardIds);
    gainable.retainAll(Arrays.asList(0, 1, 2, 3, 4, 5, 10));
    if (gainable.size() > 0) {
      return Collections.min(gainable,
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

  default Button chancellor(Button[] buttons) {
    // TODO make this not default, have strategies implement this
    assert (buttons.length == 2);
    return buttons[0];
  }

  default Button library(Button[] buttons) {
    // TODO make this not default, have strategies implement this
    assert (buttons.length == 2);
    return buttons[1];
  }

}
