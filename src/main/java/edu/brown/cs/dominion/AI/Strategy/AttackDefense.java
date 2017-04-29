package edu.brown.cs.dominion.AI.Strategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;

public class AttackDefense implements Strategy {
  // TODO make these correct, I just copied them from bmbvp
  private static List<Integer> BUY_PREFERENCES = Arrays.asList(5, 2, 4, 1),
      DISCARD_PREFERENCES = Arrays.asList(3, 4, 5, 0, 1, 2);

  @Override
  public int playAction(Game g, User who) {
    // TODO Auto-generated method stub
    return 0;
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
    if (buyable.size() > 0) {
      return Collections.min(buyable, (one, two) -> Integer
          .compare(BUY_PREFERENCES.indexOf(one), BUY_PREFERENCES.indexOf(two)));
    }

    return -1;
  }

}
