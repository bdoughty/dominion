package edu.brown.cs.dominion.action;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Feast extends AbstractAction {

  public Feast() {
    super(25, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.trash(g.getCurrentPlayer().trashFeast());
    cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.callback(ImmutableList.of(),
        g.getBoard().getCardUnderValue(5), new SelectCallback() {
          @Override
          public ClientUpdateMap call(User u, boolean inHand, int loc) {
            if (!inHand) {
              try {
                g.getPlayerFromUser(u).gain(g.gain(loc), false, false);
              } catch (EmptyPileException epe) {
                System.out.println(epe.getMessage());
              } catch (NoPileException npe) {
                System.out.println(npe.getMessage());
              }
            }

            ClientUpdateMap cm = new ClientUpdateMap(g, g.getCurrent());
            g.playerUpdateMap(cm, g.getCurrentPlayer());
            cm.piles(g.getBoard());

            return cm;
          }
        }, "feastdraw"));

  }

  @Override
  public String toString() {
    return "Feast";
  }

}
