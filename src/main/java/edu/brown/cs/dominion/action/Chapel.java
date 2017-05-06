package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.players.Player;

public class Chapel extends AbstractAction {

  public Chapel() {
    super(24, 2);
  }

  @Override
  public void play(Player p) {
    int ndiscarded = 0;
    while (ndiscarded++ < 4) {
      int selected = p.selectHand(p.getHand().stream().map(Card::getId)
      .collect(Collectors.toList()), true, "chapel");
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
