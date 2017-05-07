package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

import java.util.List;
import java.util.stream.Collectors;

public class Remodel extends AbstractAction {

  public Remodel() {
    super(12, 4);
  }

  @Override
  public void play(Player p ) {
    List<Integer> handIds = p.getHand().stream().map(Card::getId)
      .collect(Collectors.toList());
    int toTrash = 0;
    try {
      toTrash = p.selectHand(handIds, false, "remodel trash");
    } catch (UserInteruptedException e) {
      return;
    }
    Card c = p.trash(toTrash);
    List<Integer> boardIds = p.getGame().getBoard().getCardsUnderValue(c
      .getCost() + 2);
    int toGain = 0;
    try {
      toGain = p.selectBoard(boardIds, false, "remodel board");
    } catch (UserInteruptedException e) {
      return;
    }
    try {
      p.gain(p.getGame().gain(toGain), true, false);
    } catch (EmptyPileException | NoPileException ignored) {
      System.out.println("Pile is empty");
    }
  }

  @Override
  public String toString() {
    return "Remodel";
  }

}
