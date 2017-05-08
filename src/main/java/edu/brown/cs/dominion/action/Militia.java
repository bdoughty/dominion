package edu.brown.cs.dominion.action;

import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.io.send.ButtonCallback;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.UserPlayer;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(Player play) {
    play.incrementAdditionalMoney(2);
    for (Player p : play.getGame().getPlayers()) {
      if (p != play) {
        if (!p.hasMoat()) {
          new Thread(() -> {
            while (p.getHand().size() > 3) {
              ButtonCallback send = () -> {
                if (p instanceof UserPlayer) {
                  ((UserPlayer) p).sendNotify("Militia");
                }
                try {
                  if (p.getHand().size() > 3) {
                    int discard = p.selectHand(p.getHand().stream().map(Card::getId)
                      .collect(Collectors.toList()), false, "militiadiscard");
                    p.discard(discard);
                  }
                } catch (UserInteruptedException e) {
                  while (p.getHand().size() > 3) {
                    p.discard(0);
                    System.out.println("malitia interrupred");
                  }
                }
              };
              if (p instanceof UserPlayer) {
                try {
                  ((UserPlayer) p).lazySend(send);
                } catch (UserInteruptedException e) {
                  System.out.println("malitia failed to send");
                }
              } else {
                try {
                  send.pressed();
                } catch (UserInteruptedException e) {
                  System.out.println("malitia interrupred");
                }
              }
            }
          }).start();
        } else {
          p.getGame().sendMessage(p.getName() + " played Moat.");
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Militia";
  }
}
