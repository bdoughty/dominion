package edu.brown.cs.dominion.AI.Strategy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.AbstractAction;
import edu.brown.cs.dominion.players.Player;

public class DumbStrategy implements Strategy {
  private Random r = new Random();

  @Override
  public List<Integer> getDiscardPreferences(Player who) {
    return null;
  }

  @Override
  public List<Integer> getBuyPreferences(Player who) {
    return null;
  }

  @Override
  public int playAction(Player who) {
    List<Card> actions =
        who.getHand().stream().filter((card) -> card instanceof AbstractAction)
            .collect(Collectors.toList());
    if (actions.size() > 0) {
      return who.getHand()
          .indexOf(actions.get(r.nextInt(actions.size())).getId());
    }

    return -1;
  }

  @Override
  public int discard(Player who) {
    List<Card> hand = who.getHand();
    if (hand.size() > 0) {
      return r.nextInt(hand.size());
    }

    return -1;
  }

  @Override
  public int buy(int money, Player who) {
    List<Integer> buyable = who.getGame().getBoard().getCardsUnderValue(money);
    if (buyable.size() > 0) {
      return buyable.get(r.nextInt(buyable.size()));
    }

    return -1;
  }

}
