package edu.brown.cs.dominion.AI.Strategy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.action.AbstractAction;
import edu.brown.cs.dominion.games.Game;

public class DumbStrategy implements Strategy {
  private Random r = new Random();

  @Override
  public int playAction(Game g, User who) {
    List<Card> actions = g.getPlayerFromUser(who).getHand().stream()
        .filter((card) -> card instanceof AbstractAction)
        .collect(Collectors.toList());
    if (actions.size() > 0) {
      return g.getPlayerFromUser(who).getHand()
          .indexOf(actions.get(r.nextInt(actions.size())).getId());
    }

    return -1;
  }

  @Override
  public int discard(Game g, User who) {
    List<Card> hand = g.getPlayerFromUser(who).getHand();
    if (hand.size() > 0) {
      return r.nextInt(hand.size());
    }

    return -1;
  }

  @Override
  public int buy(int money, Game g, User who) {
    List<Integer> buyable = g.getBoard().getCardUnderValue(money);
    if (buyable.size() > 0) {
      return buyable.get(r.nextInt(buyable.size()));
    }

    return -1;
  }

}
