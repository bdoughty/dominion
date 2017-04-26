package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.io.send.CancelHandler;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Cellar extends AbstractAction {

  public Cellar() {
    super(7, 2);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.getCurrentPlayer().incrementActions();
    cm.requireSelectCanStop(g.getCurrent(),
        g.getCurrentPlayer().getHand().stream().map(Card::getId)
            .collect(Collectors.toList()),
        ImmutableList.of(), new DiscardOne(g, 0), new CellarDraw(g, 0));

  }
}

class DiscardOne implements SelectCallback {
  private Game g;
  private int discarded;

  public DiscardOne(Game g, int discarded) {
    this.g = g;
    this.discarded = discarded;
  }

  @Override
  public ClientUpdateMap call(User u, boolean inHand, int loc) {
    if (inHand) {
      g.getPlayerFromUser(u).discard(loc);
    }

    ClientUpdateMap cm = new ClientUpdateMap(g, u);
    cm.hand(g.getPlayerFromUser(u).getHand());
    cm.discardPileSize(g.getPlayerFromUser(u).getDiscard().size());
    cm.piles(g.getBoard());

    cm.requireSelectCanStop(u,
        g.getPlayerFromUser(u).getHand().stream().map(Card::getId)
            .collect(Collectors.toList()),
        ImmutableList.<Integer> of(), new DiscardOne(g, discarded + 1),
        new CellarDraw(g, discarded + 1));

    return cm;
  }
}

class CellarDraw implements CancelHandler {
  private Game g;
  private int draw;

  public CellarDraw(Game g, int draw) {
    this.g = g;
    this.draw = draw;
  }

  @Override
  public ClientUpdateMap cancel() {
    User currU = g.getCurrent();
    Player currP = g.getCurrentPlayer();

    currP.draw(draw);

    ClientUpdateMap cm = new ClientUpdateMap(g, currU);
    g.playerUpdateMap(cm, currP);

    return cm;
  }
}
