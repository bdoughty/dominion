package edu.brown.cs.dominion.action;


import edu.brown.cs.dominion.players.Player;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(Player p) {
    p.incrementAdditionalMoney(2);

    //TODO get all players to discard
  }

  @Override
  public String toString() {
    return "Militia";
  }
}
