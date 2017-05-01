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
import edu.brown.cs.dominion.io.send.SelectCallback;

public class Mine extends AbstractAction {

  public Mine() {
    super(10, 5);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    User currU = g.getCurrent();
    Player currP = g.getCurrentPlayer();

    List<Integer> handIds = currP.getHand().stream()
        .filter(c -> c.getMonetaryValue() != 0).map(Card::getId)
        .collect(Collectors.toList());

    SelectCallback trash = new SelectCallback() {
      @Override
      public ClientUpdateMap call(User u, boolean inHand, int loc) {
        // TODO
        assert (inHand);
        Player nestedCurrP = g.getPlayerFromUser(u);
        // how to assert this?
        assert (nestedCurrP.equals(currP));

        Card toTrash = nestedCurrP.trash(loc);
        int cost = toTrash.getCost();
        g.trash(toTrash);

        ClientUpdateMap cm1 = new ClientUpdateMap(g, u);
        g.playerUpdateMap(cm1, nestedCurrP);

        List<Integer> boardIds = g.getBoard().getMoneyUnderValue(cost + 3);

        SelectCallback gain = new SelectCallback() {
          @Override
          public ClientUpdateMap call(User u, boolean inHand, int loc) {
            assert (!inHand);
            Player nestedCurrP2 = g.getPlayerFromUser(u);
            // how to assert this?
            assert (nestedCurrP2.equals(currP));

            ClientUpdateMap cm2 = new ClientUpdateMap(g, u);

            try {
              nestedCurrP2.gain(g.gain(loc), true, false);
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

        cm1.requireSelect(u, ImmutableList.of(), boardIds, gain, "minegain");
        return cm1;
      }
    };

    cm.requireSelect(currU, handIds, ImmutableList.of(), trash, "minetrash");
  }

  @Override
  public String toString() {
    return "Mine";
  }

}
