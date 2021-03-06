package edu.brown.cs.dominion.AI.Strategy;

import java.util.Collections;
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
  public int trashForChapel(Player who) {
    if (Math.random() > 0.25)
      return -1;
    return r.nextInt(who.getHand().size() + 1) - 1;
  }

  @Override
  public int trashForRemodel(Player who) {
    return discard(who);
  }

  @Override
  public int playThroneRoom(Player who) {
    return playAction(who);
  }

  @Override
  public int playAction(Player who) {
    List<Card> actions =
        who.getHand().stream().filter((card) -> card instanceof AbstractAction)
            .collect(Collectors.toList());
    if (actions.size() > 0) {
      int toPlay = r.nextInt(actions.size());
      // System.out.println("has actions: " + Arrays.toString(actions.toArray())
      // + ", going to play: " + toPlay + ", hand is: "
      // + Arrays.toString(who.getHand().toArray()));
      return who.getHand().indexOf(actions.get(toPlay));
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
  public List<Integer> buy(int money, Player who) {
    List<Integer> buyable = who.getGame().getBoard().getCardsUnderValue(money);
    Collections.shuffle(buyable);
    return buyable;
  }

  @Override
  public int gain(List<Integer> cardIds, Player who) {
    if (cardIds.size() > 0) {
      return cardIds.get(r.nextInt(cardIds.size()));
    }

    return -1;
  }

}
