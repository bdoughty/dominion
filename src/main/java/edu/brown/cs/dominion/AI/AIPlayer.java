package edu.brown.cs.dominion.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;

import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.Button;
import edu.brown.cs.dominion.players.Player;

public class AIPlayer extends Player {
  private Strategy st;

  public AIPlayer(Game g, Strategy st) {
    super(g);
    this.st = st;
  }

  public AIPlayer(Strategy st) {
    super();
    this.st = st;
  }

  /**
   * Play a card from your hand
   *
   * @return index of card to play in hand or -1 to end action phase
   */
  @Override
  public int playHandAction() {
    return st.playAction(this);
  }

  /**
   * Buy cards
   *
   * @return list of cards ids to buy
   */
  @Override
  public List<Integer> buyCards() {
    List<Integer> out = new ArrayList<>();
    int left = getBuys();
    while (left > 0 && st.buy(getMoney(), this) != -1) {
      out.add(st.buy(getMoney(), this));
      left--;
    }

    return out;
  }

  /**
   * Select a card from your hand
   *
   * @param cardIds
   *          the ids of the cards in your hand that can be selected
   * @param cancelable
   *          boolean if the action can be canceled (return -1)
   * @param name
   *          the name of the selection that will be done
   * @return card location in hand or -1 if to cancel (but only if cancelable)
   */
  @Override
  public int selectHand(List<Integer> cardIds, boolean cancelable,
      String name) {
    if (cardIds.size() == 0 || !getHand().containsAll(cardIds)) {
      throw new RuntimeException();
    }
    switch (name) {
      case "reveal bureaucrat":
        if (cardIds.contains(6)) {
          return getHand().indexOf(6);
        } else if (cardIds.contains(3)) {
          return getHand().indexOf(3);
        } else if (cardIds.contains(4)) {
          return getHand().indexOf(4);
        } else {
          assert (cardIds.contains(5));
          return getHand().indexOf(5);
        }
      case "cellardiscard":
        int out = st.discard(this);
        assert (cardIds.contains(getHand().get(out).getId()));
        return out;
      case "chapel":
        return st.trashForChapel(this);
      case "militiadiscard":
        int o = st.discard(this);
        assert (cardIds.contains(getHand().get(o).getId()));
        return o;
      case "mine trash":
        return st.trashForMine(this);
      case "MoneyLender":
        assert (Collections.singleton(0).containsAll(cardIds));
        return getHand().indexOf(0);
      case "remodel trash":
        return st.trashForRemodel(this);
      case "throne room play":
        return st.playThroneRoom(this);
      default:
        System.out.println("help");
        throw new RuntimeException();
    }
  }

  /**
   * Select a card from the board
   *
   * @param cardIds
   *          the ids of the cards on the board that can be selected
   * @param cancelable
   *          boolean if the action can be canceled (return -1)
   * @param name
   *          the name of the selection that will be done
   * @return card id on the board -1 if to cancel (but only if cancelable)
   */
  @Override
  public int selectBoard(List<Integer> cardIds, boolean cancelable,
      String name) {
    switch (name) {
      case "feast":
        return st.gain(cardIds, this);
      case "mine board":
        return st.gain(cardIds, this);
      case "remodel board":
        return st.gain(cardIds, this);
      case "workshop":
        return st.gain(cardIds, this);
      default:
        System.out.println("help");
        throw new RuntimeException();
    }
  }

  /**
   * Press a select a button.
   *
   * @param buttons
   * @return
   */
  @Override
  public Button selectButtons(String name, Button... buttons) {
    switch (name) {
      case "chancellor":
        return st.chancellor(buttons);
      case "library keep":
        assert (buttons.length == 1);
        return buttons[0];
      case "library discard":
        assert (buttons.length == 2);
        return st.library(buttons);
      default:
        System.out.println("help");
        throw new RuntimeException();
    }
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
