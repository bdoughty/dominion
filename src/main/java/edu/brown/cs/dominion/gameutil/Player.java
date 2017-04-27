package edu.brown.cs.dominion.gameutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.action.Moat;
import edu.brown.cs.dominion.money.AbstractMoney;
import edu.brown.cs.dominion.money.Copper;
import edu.brown.cs.dominion.victory.Estate;

public class Player {
  private List<Card> deck;
  private List<Card> hand;
  private List<Card> discardPile;
  private List<Card> playedPile;

  private int buys;
  private int actions;
  private int baseMoney;
  private int additionalMoney;

  public Player() {
    this.deck = initializeDeck();
    this.hand = new ArrayList<>();
    this.discardPile = new LinkedList<>();
    this.playedPile = new LinkedList<>();
    draw(5);
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

  private void draw() throws EmptyDeckException {
    if (deck.isEmpty()) {
      if (discardPile.isEmpty()) {
        throw new EmptyDeckException("no cards left to draw");
      } else {
        deck.addAll(discardPile);
        Collections.shuffle(deck);
        discardPile.clear();
      }
    }
    Card c = deck.remove(0);
    hand.add(c);
    baseMoney += c.getMonetaryValue();
  }

  public void draw(int numCards) {
    for (int i = 0; i < numCards; i++) {
      try {
        draw();
      } catch (EmptyDeckException ede) {
        System.out.println(ede.getMessage());
      }
    }
  }

  public int adventurerDraw(List<Card> toDiscard, int moneyGained)
      throws EmptyDeckException {
    if (deck.isEmpty()) {
      if (discardPile.isEmpty()) {
        throw new EmptyDeckException("no cards left to draw");
      } else {
        deck.addAll(discardPile);
        Collections.shuffle(deck);
        discardPile.clear();
      }
    }

    Card c = deck.remove(0);

    if (c instanceof AbstractMoney) {
      hand.add(c);
      baseMoney += c.getMonetaryValue();
      return moneyGained + 1;
    } else {
      toDiscard.add(c);
      return moneyGained;
    }
  }

  public void adventurer() {
    List<Card> toDiscard = new LinkedList<>();
    int moneyGained = 0;

    while (moneyGained < 2) {
      try {
        moneyGained = adventurerDraw(toDiscard, moneyGained);
      } catch (EmptyDeckException ede) {
        System.out.println(ede.getMessage());
        break;
      }
    }

    discardPile.addAll(toDiscard);
  }

  public void buyCard(Card boughtCard) {
    discardPile.add(boughtCard);
  }

  public int getMoney() {

    return (baseMoney + additionalMoney);
  }

  public Card play(int posInHand) throws NoActionsException {
    if (actions > 0) {
      assert (posInHand >= 0 && posInHand < hand.size());
      Card c = hand.remove(posInHand);
      playedPile.add(c);
      actions--;
      return c;
    } else {
      throw new NoActionsException("No remaining actions");
    }
  }

  public Card trash(int posInHand) {
    assert (posInHand >= 0 && posInHand < hand.size());
    Card c = hand.remove(posInHand);
    baseMoney -= c.getMonetaryValue();
    return c;
  }

  public void gain(Card c, boolean toHand, boolean toDeck) {
    if (toHand) {
      hand.add(c);
      baseMoney += c.getMonetaryValue();
    } else if (toDeck) {
      deck.add(0, c);
    } else {
      discardPile.add(c);
    }
  }

  public void discard(int toDiscard) {
    assert (toDiscard >= 0 && toDiscard < hand.size());
    Card c = hand.remove(toDiscard);
    baseMoney -= c.getMonetaryValue();
    discardPile.add(c);
  }

  public Card cardToDeck(int loc) {
    assert (loc >= 0 && loc < hand.size());
    Card c = hand.remove(loc);
    deck.add(0, c);
    return c;
  }

  public void endTurn() {
    discardPile.addAll(hand);
    discardPile.addAll(playedPile);
    hand.clear();
    playedPile.clear();
    resetTurnValues();
    draw(5);
  }

  public void resetTurnValues() {
    actions = 0;
    buys = 0;
    baseMoney = 0;
    additionalMoney = 0;
  }

  public void newTurn() {
    actions = 1;
    buys = 1;
  }

  public boolean hasMoat() {
    for (Card c : hand) {
      if (c instanceof Moat) {
        return true;
      }
    }

    return false;
  }

  public void incrementActions() {
    actions++;
  }

  public void incrementBuys() {
    buys++;
  }

  public void decrementActions() {
    actions--;
  }

  public void decrementBuys() {
    buys--;
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    additionalMoney += adnlMoney;
  }

  public void decrementAdditionalMoney(int adnlMoney) {
    additionalMoney -= adnlMoney;
  }

  public int scoreDeck() {
    int points = 0;

    for (Card c : deck) {
      points += c.getVictoryPoints();
    }

    for (Card c : hand) {
      points += c.getVictoryPoints();
    }

    return points;
  }

  public void burnHand() {
    while (!hand.isEmpty()) {
      discardPile.add(hand.remove(0));
    }
  }

  public int getActions() {
    return actions;
  }

  public void setActions(int a) {
    actions = a;
  }

  public int getBuys() {
    return buys;
  }

  public List<Card> getHand() {
    return hand;
  }

  public List<Card> getDeck() {
    return deck;
  }

  public List<Card> getDiscard() {
    List<Card> discard = new LinkedList<>(discardPile);
    discard.addAll(playedPile);
    return discard;
  }
}
