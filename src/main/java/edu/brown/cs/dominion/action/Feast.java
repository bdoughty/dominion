package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Feast extends AbstractAction {

  public Feast() {
    super(25, 4);
  }

  @Override
  public void play(Player p) throws UserInteruptedException {
    p.trashFeast();
    try {
      int selected = p.selectBoard(p.getGame().getBoard().getCardsUnderValue(5),
          false, "feast");
      p.gain(p.getGame().gain(selected), false, false);
    } catch (EmptyPileException | NoPileException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Feast";
  }

}
