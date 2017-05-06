package edu.brown.cs.dominion.action;


import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.players.Player;

import java.util.stream.Collectors;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(Player play) {
    play.incrementAdditionalMoney(2);
    for(Player p : play.getGame().getPlayers()) {
      if (p != play && !p.hasMoat()) {
        new Thread(() -> {
          while (p.getHand().size() > 3) {
            int discard = p.selectHand(p.getHand().stream().map(Card::getId)
              .collect(Collectors.toList()), false, "militiadiscard");
            p.discard(discard);
          }
        }).start();
      }
    }
  }

  @Override
  public String toString() {
    return "Militia";
  }
}
