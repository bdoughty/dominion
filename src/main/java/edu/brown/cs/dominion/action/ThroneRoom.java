package edu.brown.cs.dominion.action;

import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.UserPlayer;

public class ThroneRoom extends AbstractAction {

  public ThroneRoom() {
    super(27, 4);
  }

  @Override
  public void play(Player p) {
    List<Integer> handIds =
        p.getHand().stream().filter(c -> c instanceof AbstractAction)
            .map(Card::getId).collect(Collectors.toList());
    if (handIds.isEmpty()) {
      if (p instanceof UserPlayer) {
        ((UserPlayer) p).sendNotify("You don't have any actions!");
      }
      return;
    }
    int toPlay = 0;
    try {
      toPlay = p.selectHand(handIds, true, "throne room play");
      if (toPlay == -1) {
        return;
      }
    } catch (UserInteruptedException e) {
      return;
    }
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
