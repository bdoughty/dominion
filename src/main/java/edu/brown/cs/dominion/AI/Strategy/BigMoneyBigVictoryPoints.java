package edu.brown.cs.dominion.AI.Strategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;

public class BigMoneyBigVictoryPoints implements Strategy {
  private static List<Integer> BUY_PREFERENCES = Arrays.asList(5, 2, 4, 1),
      DISCARD_PREFERENCES = Arrays.asList(3, 4, 5, 0, 1, 2);

  @Override
  public int playAction(Game g, User who) {
    return -1;
  }

  @Override
  public int discard(Game g, User who) {
    List<Integer> hand = g.getPlayerFromUser(who).getHand().stream()
        .map((card) -> card.getId()).collect(Collectors.toList());
    if (hand.size() > 0) {
      return g.getPlayerFromUser(who).getHand()
          .indexOf(Collections.min(hand,
              (one, two) -> Integer.compare(DISCARD_PREFERENCES.indexOf(one),
                  DISCARD_PREFERENCES.indexOf(two))));
    }

    return -1;
  }

  @Override
  public int buy(int money, Game g, User who) {
    List<Integer> buyable = Strategy.buyable(money, g);
    System.out.println(Arrays.toString(buyable.toArray()));
    buyable.retainAll(BUY_PREFERENCES);
    if (buyable.size() > 0) {
      System.out.println(Arrays.toString(buyable.toArray()));
      buyable.sort(Comparator.comparingInt(one -> BUY_PREFERENCES.indexOf(one)));
      System.out.println(Arrays.toString(buyable.toArray()));
      return buyable.get(0);
    }

    return -1;
  }
}
