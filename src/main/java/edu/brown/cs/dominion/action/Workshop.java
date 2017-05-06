package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Workshop extends AbstractAction {

  public Workshop() {
    super(16, 3);
  }

  @Override
  public void play(Player p) {
    int toGain = 0;
    try {
      toGain = p.selectBoard(p.getGame().getBoard().getCardUnderValue(4),
        false, "workshop");
    } catch (UserInteruptedException e) {
      return;
    }
    try {
      p.gain(p.getGame().gain(toGain), false, false);
    } catch (EmptyPileException | NoPileException e) {
      System.out.println("Empty pile");
    }
  }

  @Override
  public String toString() {
    return "Workshop";
  }

}
