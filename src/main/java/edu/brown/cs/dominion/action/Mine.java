package edu.brown.cs.dominion.action;

import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.money.AbstractMoney;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.UserPlayer;

public class Mine extends AbstractAction {

  public Mine() {
    super(10, 5);
  }

  @Override
  public void play(Player p) {
    List<Integer> handIds = p.getHand().stream()
        .filter(c -> c instanceof AbstractMoney).map(Card::getId)
        .collect(Collectors.toList());
    int toTrash = 0;
    if (!handIds.isEmpty()) {
      try {
        toTrash = p.selectHand(handIds, false, "mine trash");
      } catch (UserInteruptedException e) {
        return;
      }
    } else {
      if (p instanceof UserPlayer) {
        ((UserPlayer) p).sendNotify("No money to trash!");
      }
      return;
    }
    Card c = p.trash(toTrash);
    List<Integer> boardIds = p.getGame().getBoard()
        .getMoneyUnderValue(c.getCost() + 3);
    int toGain = 0;
    try {
      toGain = p.selectBoard(boardIds, false, "mine board");
    } catch (UserInteruptedException e) {
      p.gain(c, true, false);
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
    return "Mine";
  }

}
