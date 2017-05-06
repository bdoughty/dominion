package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.io.send.Button;
import edu.brown.cs.dominion.players.Player;

public class Chancellor extends AbstractAction {
  public Chancellor() {
    super(23, 3);
  }

  @Override
  public void play(Player p) {
    p.incrementAdditionalMoney(2);
    Button b1 = new Button("Discard Deck", p::discardDeck);
    Button b2 =  new Button("Don't Discard Deck", () -> {});
    p.selectButtons(b1, b2).pressed();
  }

  @Override
  public String toString() {
    return "Chancellor";
  }

}
