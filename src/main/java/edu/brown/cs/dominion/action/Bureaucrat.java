package edu.brown.cs.dominion.action;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.UserPlayer;
import edu.brown.cs.dominion.victory.AbstractVictoryPoint;

public class Bureaucrat extends AbstractAction {

  public Bureaucrat() {
    super(22, 4);
  }

  @Override
  public void play(Player play) {
    try {
      play.gain(play.getGame().gain(1), false, true);
    } catch (EmptyPileException | NoPileException e) {
      System.out.println(e.getMessage());
    }

    List<Player> players = new LinkedList<>(play.getGame().getPlayers());
    players.remove(play);

    for (Player p : players) {
      if (p.hasMoat()) {
        p.getGame().sendMessage(p.getName() + " played Moat.");
      } else {
        List<Integer> vpCards = p.getHand().stream()
            .filter((card) -> card instanceof AbstractVictoryPoint)
            .map(Card::getId).collect(Collectors.toList());
        if (!vpCards.isEmpty()) {
          new Thread(() -> {
            sendNotification(p, "Bureaucrat");
            try {
              int loc = p.selectHand(vpCards, false, "reveal bureaucrat");
              Card c = p.cardToDeck(loc);
              p.getGame().sendMessage(p.getName() + " put " + c.toString()
                  + " on top of their deck.");
            } catch (UserInteruptedException uie) {
            }
          }).start();
        }
      }
    }
  }

  // private void sendGameMessge(Game g, String message) {
  // if (g instanceof UserGame) {
  // ((UserGame) g).sendServerMessage(message);
  // }
  // }

  private void sendNotification(Player p, String message) {
    if (p instanceof UserPlayer) {
      ((UserPlayer) p).sendNotify(message);
    }
  }

  @Override
  public String toString() {
    return "Bureaucrat";
  }
}
