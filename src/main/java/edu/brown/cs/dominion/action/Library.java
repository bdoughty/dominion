package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.EmptyDeckException;
import edu.brown.cs.dominion.io.send.Button;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Library extends AbstractAction {

  public Library() {
    super(28, 5);
  }

  @Override
  public void play(Player p) {
    while (p.getHand().size() < 7) {
      try {
        Card justDrawn = p.drawOne();
        Button b1 = new Button("Keep " + justDrawn.toString(), () -> {
        });
        Button b2 = new Button("Discard " + justDrawn.toString(), () -> {
          p.discard(p.getHand().size() - 1);
        });
        if (justDrawn instanceof AbstractAction) {
          p.selectButtons("library discard", b1, b2).pressed();
        } else {
          p.selectButtons("library keep", b1).pressed();
        }
      } catch (EmptyDeckException | UserInteruptedException e) {
        System.out.println(e.getMessage());
        break;
      }
    }
  }

  @Override
  public String toString() {
    return "Library";
  }

}
