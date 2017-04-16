package edu.brown.cs.dominion.gameutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.victory.Estate;

public class Player {

  private int id;
  private String name;

  private List<Card> deck;
  private List<Card> hand;
  private List<Card> discardPile;
  private List<Card> playedPile;

  private int buys;
  private int actions;
  private int baseMoney;
  private int additionalMoney;

  public Player(int id) {
    this.id = id;
    this.name = null;
    this.deck = initializeDeck();
    this.hand = new ArrayList<>();
    this.discardPile = new LinkedList<>();
    this.playedPile = new LinkedList<>();
  }

  private List<Card> initializeDeck() {
    List<Card> deck = new LinkedList<>();

    for (int i = 0; i < 7; i++) {
      deck.add(new Copper());
    }

    for (int i = 0; i < 3; i++) {
      deck.add(new Estate());
    }

    Collections.shuffle(deck);

    return deck;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private void draw() {
    if (this.deck.isEmpty()) {
      if (this.discardPile.isEmpty()) {
        throw new UnsupportedOperationException("no cards left to draw");
      } else {
        this.deck.addAll(this.discardPile);
        Collections.shuffle(this.deck);
        this.discardPile.clear();
      }
    }

    Card c = this.deck.remove(0);
    this.hand.add(c);
  }

  public void draw(int numCards) {
    for (int i = 0; i < numCards; i++) {
      draw();
    }
  }

  public void buyCard(Card boughtCard) {
    this.discardPile.add(boughtCard);
  }

  public int getMoney() {
    return (baseMoney + additionalMoney);
  }

  public void discard(List<Integer> toDiscard) {
    assert (Collections.max(toDiscard) < this.hand.size()
        && Collections.min(toDiscard) >= 0);

    Collections.sort(toDiscard, Collections.reverseOrder());

    for (int i : toDiscard) {
      Card c = this.hand.remove(i);
      this.discardPile.add(c);
    }
  }

  public void endTurn() {
    this.discardPile.addAll(this.hand);
    this.discardPile.addAll(this.playedPile);
    this.hand.clear();
    this.playedPile.clear();
    resetTurnValues();
    draw(5);
  }

  public void resetTurnValues() {
    this.actions = 0;
    this.buys = 0;
    this.baseMoney = 0;
    this.additionalMoney = 0;
  }

  public void newTurn() {
    this.actions = 1;
    this.buys = 1;

    for (Card c : this.hand) {
      this.baseMoney += c.getMonetaryValue();
    }

  }

  public boolean hasMoat() {
    for (Card c : this.hand) {
      if (c.getId() == 11) {
        return true;
      }
    }

    return false;
  }

  public void incrementActions() {
    this.actions++;
  }

  public void incrementBuys() {
    this.buys++;
  }

  public void decrementActions() {
    this.actions--;
  }

  public void decrementBuys() {
    this.buys--;
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    this.additionalMoney += adnlMoney;
  }

  public void decrementAdditionalMoney(int adnlMoney) {
    this.additionalMoney -= adnlMoney;
  }

  public int scoreDeck() {
    int points = 0;

    for (Card c : this.deck) {
      points += c.getVictoryPoints();
    }

    for (Card c : this.hand) {
      points += c.getVictoryPoints();
    }

    return points;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Player) {
      Player that = (Player) o;
      return (this.id == that.id);
    } else {
      return false;
    }
  }

}