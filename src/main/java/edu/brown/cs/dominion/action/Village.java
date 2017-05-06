package edu.brown.cs.dominion.action;


import edu.brown.cs.dominion.players.Player;

public class Village extends AbstractAction {

  public Village() {
    super(14, 3);
  }

  @Override
  public void play(Player p) {
    p.incrementActions();
    p.incrementActions();
    p.draw(1);
  }

  @Override
  public String toString() {
    return "Village";
  }

}
