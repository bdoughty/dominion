package edu.brown.cs.dominion.action;


import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;

public class Feast extends AbstractAction {

  public Feast() {
    super(25, 4);
  }

  @Override
  public void play(Player p) {
    p.trashFeast();
    int selected = p.selectBoard(p.getGame().getBoard().getCardUnderValue(5)
      , false, "feast");
    try {
      p.gain(p.getGame().gain(selected), false, false);
    } catch (EmptyPileException | NoPileException epe) {
      System.out.println(epe.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Feast";
  }

}
