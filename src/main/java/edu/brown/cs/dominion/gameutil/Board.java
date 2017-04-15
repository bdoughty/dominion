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

  public Card buyCard(int id) {
    if (piles.containsKey(id)) {
      return piles.get(id).draw();
    } else {
      throw new IllegalArgumentException("No card with id: " + id);
    }
  }

  public void trashCard(Card c) {
    trash.add(c);
  }

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
