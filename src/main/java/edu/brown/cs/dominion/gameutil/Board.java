package edu.brown.cs.dominion.gameutil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.money.Gold;
import edu.brown.cs.dominion.money.Silver;
import edu.brown.cs.dominion.victory.Curse;
import edu.brown.cs.dominion.victory.Duchy;
import edu.brown.cs.dominion.victory.Estate;
import edu.brown.cs.dominion.victory.Province;

public class Board {

  private Map<Integer, Pile> piles;

  private List<Card> trash;

  /**
   * Constructor for a board. Takes in a list of ids for which action cards
   * should be used this game and initializes the necessary piles by calling
   * boardSetUp().
   *
   * @param actionCardIds
   *          list (of length 10) of action card ids
   */
  public Board(List<Integer> actionCardIds) {
    assert (actionCardIds.size() == 10);
    boardSetUp(actionCardIds);
  }

  private void boardSetUp(List<Integer> actionCardIds) {
    piles = new HashMap<>();

    // building board
    // money
    piles.put(0, new Pile(Copper::new, 60));
    piles.put(1, new Pile(Silver::new, 40));
    piles.put(2, new Pile(Gold::new, 30));

    // victory points
    piles.put(3, new Pile(Estate::new, 24));
    piles.put(4, new Pile(Duchy::new, 12));
    piles.put(5, new Pile(Province::new, 12));
    piles.put(6, new Pile(Curse::new, 30));

    // actions
    for (int i = 0; i < actionCardIds.size(); i++) {
      int id = actionCardIds.get(i);
      piles.put(id, new Pile(CardFactory.getInstance().getFactory(id), 10));
    }

    trash = new LinkedList<>();
  }

  /**
   * Buys a card from the board given an integer value.
   *
   * @param id
   *          id of the card to be purchased
   * @return the purchased card
   */
  public Card buyCard(int id, int currMoney) {
    if (piles.containsKey(id)) {
      if (piles.get(id).getCost() <= currMoney) {
        return piles.get(id).draw();
      } else {
        throw new TooExpensiveException("Can't afford card " + id);
      }
    } else {
      throw new IllegalArgumentException("No card with id: " + id);
    }
  }

  /**
   * Adds a card from the users hand to the trash pile.
   *
   * @param c
   *          card to be trashed
   */
  public void trashCard(Card c) {
    trash.add(c);
  }

  /**
   * Determines whether the game termination conditions have been met.
   *
   * @return true, if the game is over (3 piles or Provinces exhausted) or false
   *         if not
   */
  public boolean gameHasEnded() {
    int emptyPiles = 0;

    for (Pile p : piles.values()) {
      if (p.isEmpty()) {
        emptyPiles++;
      }
    }

    return piles.get(5).isEmpty() || emptyPiles >= 3;
  }

}