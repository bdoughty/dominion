package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.io.send.CancelHandler;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Chapel extends AbstractAction {

  public Chapel() {
    super(24, 2);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.callback(
        g.getCurrentPlayer().getHand().stream().map(Card::getId)
            .collect(Collectors.toList()),
        ImmutableList.of(), new TrashOne(g, 1), new CancelChapel(g),
      "Chapel"));

  }

  @Override
  public String toString() {
    return "Chapel";
  }
}

class TrashOne implements SelectCallback {
  private Game g;
  private int trashed;

  public TrashOne(Game g, int trashed) {
    this.g = g;
    this.trashed = trashed;
  }

  @Override
  public ClientUpdateMap call(User u, boolean inHand, int loc) {
    if (inHand) {
      g.trash(g.getPlayerFromUser(u).trash(loc));
    }

    ClientUpdateMap cm = new ClientUpdateMap(g, u);
    cm.hand(g.getPlayerFromUser(u).getHand());
    cm.piles(g.getBoard());

    if (trashed < 4) {
      cm.requirePlayerAction(u, RequirePlayerAction.callback(
          g.getPlayerFromUser(u).getHand().stream().map(Card::getId)
              .collect(Collectors.toList()),
          ImmutableList.<Integer> of(), new TrashOne(g, trashed + 1),
          new CancelChapel(g), "cancel chaple"));
    }

    return cm;
  }
}

class CancelChapel implements CancelHandler {
  private Game g;

  public CancelChapel(Game g) {
    this.g = g;
  }

  @Override
  public ClientUpdateMap cancel() {
    User currU = g.getCurrent();
    Player currP = g.getCurrentPlayer();

    ClientUpdateMap cm = new ClientUpdateMap(g, currU);
    g.playerUpdateMap(cm, currP);

    return cm;
  }
}
