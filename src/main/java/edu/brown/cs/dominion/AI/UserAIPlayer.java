package edu.brown.cs.dominion.AI;

import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.games.UserGame;
import edu.brown.cs.dominion.gameutil.EmptyDeckException;
import edu.brown.cs.dominion.gameutil.NoActionsException;

public class UserAIPlayer extends AIPlayer {
  private Strategy st;

  public UserAIPlayer(Game g, Strategy st) {
    super(g, st);
  }

  public UserGame getUserGame(){
    return (UserGame) getGame();
  }

  @Override
  public void discard(int loc) {
    super.discard(loc);
    getUserGame().sendAllHandSize(this);
  }

  @Override
  public void draw(int numCards) {
    super.draw(numCards);
    getUserGame().sendAllHandSize(this);
  }

  @Override
  public Card drawOne() throws EmptyDeckException {
    Card c = super.drawOne();
    getUserGame().sendAllHandSize(this);
    return c;
  }

  @Override
  public void adventurer() {
    super.adventurer();
    getUserGame().sendAllHandSize(this);
  }

  @Override
  public Card play(int posInHand) throws NoActionsException {
    Card c = super.play(posInHand);
    getUserGame().sendAllHandSize(this);
    return c;
  }

  @Override
  public Card trash(int posInHand) {
    Card c = super.trash(posInHand);
    getUserGame().sendAllHandSize(this);
    return c;
  }

  @Override
  public void gain(Card c, boolean toHand, boolean toDeck) {
    super.gain(c, toHand, toDeck);
    getUserGame().sendAllHandSize(this);
  }

  @Override
  public Card cardToDeck(int loc) {
    Card c = super.cardToDeck(loc);
    getUserGame().sendAllHandSize(this);
    return c;
  }

  @Override
  public void endTurn() {
    super.endTurn();
    getUserGame().sendAllHandSize(this);
  }

  @Override
  public void setActions(int a) {
    super.setActions(a);
    getUserGame().sendAllHandSize(this);
  }
}
