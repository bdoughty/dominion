package edu.brown.cs.dominion.action;

import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.players.Player;

public class ThroneRoom extends AbstractAction {

  public ThroneRoom() {
    super(27, 4);
  }

  @Override
  public void play(Player p) {
    List<Integer> handIds = p.getHand().stream()
        .filter(c -> c instanceof AbstractAction).map(Card::getId)
        .collect(Collectors.toList());
    int toPlay = p.selectHand(handIds, false, "throne room play");
    p.incrementActions();
    Card c;
    try {
      c = p.play(toPlay);
      c.play(p);
      c.play(p);
    } catch (NoActionsException | NotActionException nae) {
      System.out.println(nae.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Throne Room";
  }
}
