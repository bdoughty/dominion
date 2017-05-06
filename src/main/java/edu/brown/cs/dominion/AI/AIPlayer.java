package edu.brown.cs.dominion.AI;

import com.google.gson.JsonObject;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.Button;
import edu.brown.cs.dominion.players.Player;

import java.util.List;

public class AIPlayer extends Player {
  private Strategy st = new DumbStrategy();

  public AIPlayer(Game g, Strategy st) {
    super(g);
    this.st = st;
  }

  /*
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
  */

  /*
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
  */

  /**
   * Play a card from your hand
   * @return  index of card to play in hand or -1 to end action phase
   */
  @Override
  public int playHandAction() {
    return 0;
  }

  /**
   * Buy cards
   * @return list of cards ids to buy
   */
  @Override
  public List<Integer> buyCards() {
    return null;
  }

  /**
   * Select a card from your hand
   * @param cardIds the ids of the cards in your hand that can be selected
   * @param cancelable boolean if the action can be canceled (return -1)
   * @param name the name of the selection that will be done
   * @return card location in hand or -1 if to cancel (but only if cancelable)
   */
  @Override
  public int selectHand(List<Integer> cardIds, boolean cancelable, String name) {
    return 0;
  }

  /**
   * Select a card from your hand
   * @param cardIds the ids of the cards on the board that can be selected
   * @param cancelable boolean if the action can be canceled (return -1)
   * @param name the name of the selection that will be done
   * @return card id on the board -1 if to cancel (but only if
   * cancelable)
   */
  @Override
  public int selectBoard(List<Integer> cardIds, boolean cancelable, String name) {
    return 0;
  }

  /**
   * Press a select a button.
   * @param buttons
   * @return
   */
  @Override
  public Button selectButtons(Button... buttons) {
    return null;
  }

  @Override
  public JsonObject toJson() {
    JsonObject main = new JsonObject();
    main.addProperty("id", getId());
    main.addProperty("color", "#444");
    main.addProperty("name", "Computer " + getId());
    return main;
  }

  @Override
  public String getName() {
    return "Computer " + getId();
  }

}
