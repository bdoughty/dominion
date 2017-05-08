package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;

public class Cellar extends AbstractAction {

  public Cellar() {
    super(7, 2);
  }

  @Override
  public void play(Player p) throws UserInteruptedException {
    p.incrementActions();
    int cardsDiscarded = 0;
    int discard = 0;
    while (discard != -1) {
      try {
        discard = p.selectHand(
            p.getHand().stream().map(Card::getId).collect(Collectors.toList()),
            true, "cellardiscard");
        System.out.println("Discard " + discard);
        if (discard != -1) {
          p.discard(discard);
          cardsDiscarded++;
        }
      } catch (UserInteruptedException uie) {
        p.draw(cardsDiscarded);
        throw uie;
      }
    }
    p.draw(cardsDiscarded);
  }

  @Override
  public String toString() {
    return "Cellar";
  }
}
