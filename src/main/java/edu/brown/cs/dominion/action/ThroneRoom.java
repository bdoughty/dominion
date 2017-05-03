package edu.brown.cs.dominion.action;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class ThroneRoom extends AbstractAction {

  public ThroneRoom() {
    super(27, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    SelectCallback playTwice = new SelectCallback() {
      @Override
      public ClientUpdateMap call(User u, boolean inHand, int loc) {
        Player currP = g.getPlayerFromUser(u);
        if (inHand) {
          currP.incrementActions();
          try {
            // TODO this
            Card c = currP.play(loc);
            c.play(g, cm);
            c.play(g, cm);
          } catch (NoActionsException | NotActionException nae) {
            System.out.println(nae.getMessage());
          }
          return cm;
        } else {
          return null;
        }
      }
    };

    List<Integer> actions = g.getCurrentPlayer().getHand().stream()
        .filter(c -> c instanceof AbstractAction).map(Card::getId)
        .collect(Collectors.toList());

    if (!actions.isEmpty()) {
      cm.requireSelect(g.getCurrent(), actions, ImmutableList.of(), playTwice,
          "throneroomplay");
    }
  }

  @Override
  public String toString() {
    return "Throne Room";
  }
}
