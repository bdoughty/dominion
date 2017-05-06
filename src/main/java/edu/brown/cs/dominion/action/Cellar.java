package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.players.Player;

import java.util.stream.Collectors;

public class Cellar extends AbstractAction {

  public Cellar() {
    super(7, 2);
  }

  @Override
  public void play(Player p) {
    p.incrementActions();
    int cardsDiscarded = 0;
    int discard = 0;
    while (discard != -1) {
      discard = p.selectHand(
        p.getHand().stream().map(Card::getId).collect(Collectors.toList()),
        true, "cellardiscard");
      p.discard(discard);
      cardsDiscarded++;
    }
    p.draw(cardsDiscarded);
  }

  @Override
  public String toString() {
    return "Cellar";
  }
}
