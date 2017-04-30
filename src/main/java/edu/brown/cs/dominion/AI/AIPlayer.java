package edu.brown.cs.dominion.AI;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.io.send.ButtonCall;
import edu.brown.cs.dominion.io.send.Callback;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class AIPlayer extends User implements AI {
  private Strategy st = new DumbStrategy();

  public AIPlayer(int id, int startId, Strategy st) {
    super(id, startId);
    this.st = st;
  }

  @Override
  public void play(Game g) {
    System.out.println("started the game!!!!!");

    while (g.getPlayerFromUser(this).getActions() > 0) {
      int actionLoc = st.playAction(g, this);

      if (actionLoc == -1) {
        break;
      }
    }

    g.endActionPhase(this);

    int buys = g.getPlayerFromUser(this).getBuys();
    int money = g.getPlayerFromUser(this).getMoney();

    List<Integer> toBuy = new LinkedList<>();

    while (buys > 0) {
      int buyId = st.buy(money, g, this);

      if (buyId == -1) {
        break;
      }

      try {
        toBuy.add(buyId);
        buys--;
        money -= g.getBoard().getCostFromId(buyId);
      } catch (NoPileException npe) {
        System.out.println(npe.getMessage());
        break;
      }
    }

    g.endBuyPhase(this, toBuy);
  }

  //WARNING, C might = null or buttons might be empty
  @Override
  public void doCallback(Game g, Callback c, List<ButtonCall> buttons) {
    // TODO Switch on callback string and act accordingly (buying cards, gaining
    // cards, discarding, etc.)
    // TODO how to avoid race conditions on multi-stage actions?? (i think
    // its fine)
    // switch (c.getName()) {
    // case "Militia":
    // st.discard(g, this);
    // st.discard(g, this);
    // case "Workshop":
    //
    //
    // }

  }
}
