package edu.brown.cs.dominion.gameutil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.money.Gold;
import edu.brown.cs.dominion.money.Money;
import edu.brown.cs.dominion.money.Silver;
import edu.brown.cs.dominion.victory.Curse;
import edu.brown.cs.dominion.victory.Duchy;
import edu.brown.cs.dominion.victory.Estate;
import edu.brown.cs.dominion.victory.Province;
import edu.brown.cs.dominion.victory.VictoryPoint;

public class Board {

  private Pile coppers;
  private Pile silvers;
  private Pile golds;

  private Pile estates;
  private Pile duchies;
  private Pile provinces;
  private Pile curses;

  private List<Pile> actions;

  private List<Pile> allPiles;

  private List<Card> trash;

  public Board(List<Integer> actionCardIds) {
    assert (actionCardIds.size() == 10);
    boardSetUp(actionCardIds);
  }

  private void boardSetUp(List<Integer> actionCardIds) {
    this.coppers = new Pile(Copper::new, 60);
    this.silvers = new Pile(Silver::new, 40);
    this.golds = new Pile(Gold::new, 30);

    this.estates = new Pile(Estate::new, 24);
    this.duchies = new Pile(Duchy::new, 12);
    this.provinces = new Pile(Province::new, 12);
    this.curses = new Pile(Curse::new, 30);

    this.actions = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      this.actions.add(new Pile(
          CardFactory.getInstance().getFactory(actionCardIds.get(i)), 10));
    }

    this.allPiles.addAll(this.actions);
    this.allPiles.add(this.coppers);
    this.allPiles.add(this.silvers);
    this.allPiles.add(this.golds);
    this.allPiles.add(this.estates);
    this.allPiles.add(this.duchies);
    this.allPiles.add(this.provinces);
    this.allPiles.add(this.curses);

    this.trash = new LinkedList<>();
  }

  public Card buyMoney(Money m) {
    switch (m) {
      case COPPER:
        return this.coppers.draw();
      case SILVER:
        return this.silvers.draw();
      case GOLD:
        return this.golds.draw();
      default:
        throw new UnsupportedOperationException(
            "can't buy money of this type!");
    }
  }

  public Card buyVictoryPoint(VictoryPoint vp) {
    switch (vp) {
      case ESTATE:
        return this.estates.draw();
      case DUCHY:
        return this.duchies.draw();
      case PROVINCE:
        return this.provinces.draw();
      case CURSE:
        return this.curses.draw();
      default:
        throw new UnsupportedOperationException(
            "can't buy victory point of this type!");
    }
  }

  public Card buyAction(int i) {
    assert (i >= 0 && i < 10);
    return this.actions.get(i).draw();
  }

  public void trashCard(Card c) {
    this.trash.add(c);
  }

  public boolean gameHasEnded() {
    int emptyPiles = 0;

    for (Pile p : this.allPiles) {
      if (p.isEmpty()) {
        emptyPiles++;
      }
    }

    return this.provinces.isEmpty() || emptyPiles >= 3;
  }

}
