package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.io.send.SelectCallback;
import edu.brown.cs.dominion.money.Copper;

public class Moneylender extends AbstractAction {

  public Moneylender() {
    super(26, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    SelectCallback trash = new SelectCallback() {
      @Override
      public ClientUpdateMap call(User u, boolean inHand, int loc) {
        if (inHand) {
          g.trash(g.getPlayerFromUser(u).trash(loc));
          g.getPlayerFromUser(u).incrementAdditionalMoney(3);
          ClientUpdateMap cm1 = new ClientUpdateMap(g, u);
          g.playerUpdateMap(cm1, g.getPlayerFromUser(u));
          return cm1;
        } else {
          return null;
        }
      }
    };

    cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.callback(
        g.getCurrentPlayer().getHand().stream().filter(c -> c instanceof Copper)
            .map(Card::getId).collect(Collectors.toList()),
        ImmutableList.of(), trash, () -> null, "moneylendertrash"));
  }

  @Override
  public String toString() {
    return "Moneylender";
  }
}
