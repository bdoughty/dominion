package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.ButtonCallback;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Library extends AbstractAction {

  public Library() {
    super(28, 5);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    if (g.getCurrentPlayer().getHand().size() < 7) {
      g.getCurrentPlayer().draw(1);

      Card justDrawn = g.getCurrentPlayer().getHand()
          .get(g.getCurrentPlayer().getHand().size() - 1);

      cm.putButton(g.getCurrent(), "Keep " + justDrawn.toString(),
          new LibraryKeep(g));

      if (justDrawn instanceof AbstractAction) {
        cm.putButton(g.getCurrent(), "Discard " + justDrawn.toString(),
            new LibraryDiscard(g));
      }
    }
  }

  @Override
  public String toString() {
    return "Library";
  }

}

class LibraryKeep implements ButtonCallback {

  private Game g;

  LibraryKeep(Game g) {
    this.g = g;
  }

  @Override
  public ClientUpdateMap clicked(User u) {
    if (g.getPlayerFromUser(u).getHand().size() < 7) {
      g.getPlayerFromUser(u).draw(1);

      Card justDrawn = g.getPlayerFromUser(u).getHand()
          .get(g.getPlayerFromUser(u).getHand().size() - 1);

      ClientUpdateMap cm = new ClientUpdateMap(g, u);

      g.playerUpdateMap(cm, g.getPlayerFromUser(u));

      cm.putButton(u, "Keep " + justDrawn.toString(), new LibraryKeep(g));

      if (justDrawn instanceof AbstractAction) {
        cm.putButton(u, "Discard " + justDrawn.toString(),
            new LibraryDiscard(g));
      }

      return cm;
    } else {
      return null;
    }
  }
}

class LibraryDiscard implements ButtonCallback {

  private Game g;

  LibraryDiscard(Game g) {
    this.g = g;
  }

  @Override
  public ClientUpdateMap clicked(User u) {
    g.getPlayerFromUser(u).discard(g.getPlayerFromUser(u).getHand().size() - 1);

    if (g.getPlayerFromUser(u).getHand().size() < 7) {
      g.getPlayerFromUser(u).draw(1);

      Card justDrawn = g.getPlayerFromUser(u).getHand()
          .get(g.getPlayerFromUser(u).getHand().size() - 1);

      ClientUpdateMap cm = new ClientUpdateMap(g, u);

      g.playerUpdateMap(cm, g.getPlayerFromUser(u));

      cm.putButton(u, "Keep " + justDrawn.toString(), new LibraryKeep(g));

      if (justDrawn instanceof AbstractAction) {
        cm.putButton(u, "Discard " + justDrawn.toString(),
            new LibraryDiscard(g));
      }

      return cm;
    } else {
      return null;
    }
  }

}