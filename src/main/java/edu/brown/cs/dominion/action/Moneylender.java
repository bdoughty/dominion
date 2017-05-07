package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Moneylender extends AbstractAction {

  public Moneylender() {
    super(26, 4);
  }

  @Override
  public void play(Player p) {
    int toTrash = 0;
    try {
      toTrash = p
          .selectHand(
              p.getHand().stream().filter(c -> c instanceof Copper)
                  .map(Card::getId).collect(Collectors.toList()),
              true, "MoneyLender");
    } catch (UserInteruptedException e) {
      return;
    }
    if (toTrash >= 0) {
      p.trash(toTrash);
      p.incrementAdditionalMoney(3);
    }
  }

  @Override
  public String toString() {
    return "Moneylender";
  }
}
