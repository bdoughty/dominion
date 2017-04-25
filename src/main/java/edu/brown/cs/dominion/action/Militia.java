package edu.brown.cs.dominion.action;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.incrementAdditionalMoney(2);

    List<User> users = new LinkedList<>(g.getAllUsers());
    users.remove(g.getCurrent());

    for (User user : users) {
      if (g.getPlayerFromUser(user).getHand().size() > 3) {
        cm.requireSelect(user,
            g.getPlayerFromUser(user).getHand().stream().map(Card::getId)
                .collect(Collectors.toList()),
            ImmutableList.<Integer> of(), new DownToThree(g));
      }
    }
  }
}

class DownToThree implements SelectCallback {
  private Game g;

  public DownToThree(Game g) {
    this.g = g;
  }

  @Override
  public ClientUpdateMap call(User u, boolean inHand, int loc) {
    if (inHand) {
      g.getPlayerFromUser(u).trash(loc);
    }

    ClientUpdateMap cm = new ClientUpdateMap(g, u);
    cm.hand(g.getPlayerFromUser(u).getHand());

    if (g.getPlayerFromUser(u).getHand().size() > 3) {
      cm.requireSelect(u,
          g.getPlayerFromUser(u).getHand().stream().map(Card::getId)
              .collect(Collectors.toList()),
          ImmutableList.<Integer> of(), new DownToThree(g));
    }

    return cm;
  }
}
