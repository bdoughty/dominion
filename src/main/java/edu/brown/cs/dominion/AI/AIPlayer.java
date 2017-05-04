package edu.brown.cs.dominion.AI;

import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.io.send.ButtonCall;
import edu.brown.cs.dominion.io.send.Callback;

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

  // WARNING, C might = null or buttons might be empty
  @Override
  public void doCallback(Game g, Callback c, List<ButtonCall> buttons) {
    if (!c.equals(null)) {
      switch (c.getName()) {
        case "militiadiscard":
          int toDiscard = st.discard(g, this);
          if (toDiscard >= 0) {
            c.getCallback().call(this, true, toDiscard);
          }
          break;
      }
    }
  }
}
