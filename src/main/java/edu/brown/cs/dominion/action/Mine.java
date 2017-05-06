package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Mine extends AbstractAction {

  public Mine() {
    super(10, 5);
  }

  @Override
  public void play(Player p) {
    List<Integer> handIds = p.getHand().stream()
        .filter(c -> c.getMonetaryValue() != 0).map(Card::getId)
        .collect(Collectors.toList());
    int toTrash = p.selectHand(handIds, false, "mine trash");
    Card c = p.trash(toTrash);
    List<Integer> boardIds = p.getGame().getBoard()
      .getMoneyUnderValue(c.getCost() + 3);
    int toGain = p.selectBoard(boardIds, false, "mine board");
    try {
      p.gain(p.getGame().gain(toGain), true, false);
    } catch (EmptyPileException | NoPileException ignored) {
      System.out.println("Pile is empty");
    }
  }

  @Override
  public String toString() {
    return "Mine";
  }

}
