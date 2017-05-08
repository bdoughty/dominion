package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Chapel extends AbstractAction {

  public Chapel() {
    super(24, 2);
  }

  @Override
  public void play(Player p) throws UserInteruptedException {
    int ndiscarded = 0;
    while (ndiscarded++ < 4) {
      int selected = p.selectHand(
          p.getHand().stream().map(Card::getId).collect(Collectors.toList()),
          true, "chapel");
      if (selected == -1) {
        break;
      } else {
        p.trash(selected);
      }
    }
  }

  @Override
  public String toString() {
    return "Chapel";
  }
}
