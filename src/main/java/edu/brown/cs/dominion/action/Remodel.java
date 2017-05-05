package edu.brown.cs.dominion.action;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Remodel extends AbstractAction {

  public Remodel() {
    super(12, 4);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    User currU = g.getCurrent();
    Player currP = g.getCurrentPlayer();

    List<Integer> handIds = currP.getHand().stream().map(Card::getId)
        .collect(Collectors.toList());

    SelectCallback trash = new SelectCallback() {
      @Override
      public ClientUpdateMap call(User u, boolean inHand, int loc) {
        assert (inHand);
        Player nestedCurrP = g.getPlayerFromUser(u);
        // how to assert this?
        assert (nestedCurrP.equals(currP));

        Card toTrash = nestedCurrP.trash(loc);
        int cost = toTrash.getCost();
        g.trash(toTrash);

        ClientUpdateMap cm1 = new ClientUpdateMap(g, u);
        g.playerUpdateMap(cm1, nestedCurrP);

        List<Integer> boardIds = g.getBoard().getCardUnderValue(cost + 2);

        SelectCallback gain = new SelectCallback() {
          @Override
          public ClientUpdateMap call(User u, boolean inHand, int loc) {
            assert (!inHand);
            Player nestedCurrP2 = g.getPlayerFromUser(u);
            // how to assert this?
            assert (nestedCurrP2.equals(currP));

            ClientUpdateMap cm2 = new ClientUpdateMap(g, u);

            try {
              nestedCurrP2.gain(g.gain(loc), false, false);
            } catch (EmptyPileException epe) {
              System.out.println(epe.getMessage());
            } catch (NoPileException npe) {
              System.out.println(npe.getMessage());
            }

            g.playerUpdateMap(cm2, nestedCurrP2);
            cm2.piles(g.getBoard());

            return cm2;
          }
        };

        cm1.requirePlayerAction(u, RequirePlayerAction.callback(ImmutableList.of(), boardIds, gain,
          "remodelgain"));
        return cm1;
      }
    };

    cm.requirePlayerAction(currU, RequirePlayerAction.callback(handIds, ImmutableList.of(), trash,
      "remodeltrash"));
  }

  @Override
  public String toString() {
    return "Remodel";
  }

}
