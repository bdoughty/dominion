package edu.brown.cs.dominion.action;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.SelectCallback;
import edu.brown.cs.dominion.victory.AbstractVictoryPoint;

public class Bureaucrat extends AbstractAction {

  public Bureaucrat() {
    super(22, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    try {
      g.getCurrentPlayer().gain(g.gain(1), false, true);
    } catch (EmptyPileException | NoPileException e) {
      System.out.println(e.getMessage());
    }

    List<User> users = new LinkedList<>(g.getAllUsers());
    users.remove(g.getCurrent());

    for (User user : users) {
      if (g.getPlayerFromUser(user).hasMoat()) {
        g.sendServerMessage(user.getName() + " played Moat.");
      } else {
        cm.requireSelect(user,
            g.getPlayerFromUser(user).getHand().stream()
                .filter((card) -> card instanceof AbstractVictoryPoint)
                .map(Card::getId).collect(Collectors.toList()),
            ImmutableList.<Integer> of(), new RevealOne(g), "bearocrats");
      }
    }
  }

  @Override
  public String toString() {
    return "Bureaucrat";
  }
}

class RevealOne implements SelectCallback {
  private Game g;

  public RevealOne(Game g) {
    this.g = g;
  }

  @Override
  public ClientUpdateMap call(User u, boolean inHand, int loc) {
    if (inHand) {
      Card c = g.getPlayerFromUser(u).cardToDeck(loc);
      g.sendServerMessage(
          u.getName() + " put " + c.toString() + " on top of their deck.");
    }

    ClientUpdateMap cm = new ClientUpdateMap(g, u);
    cm.hand(g.getPlayerFromUser(u).getHand());
    cm.deckRemaining(g.getPlayerFromUser(u).getDeck().size());

    return cm;
  }
}
