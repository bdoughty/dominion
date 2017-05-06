package edu.brown.cs.dominion.players;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.UserGame;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.Callback;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

import static edu.brown.cs.dominion.io.send.MessageType.*;
import static edu.brown.cs.dominion.players.PlayerWake.*;

/**
 * Created by henry on 5/5/2017.
 */
public class UserPlayer extends Player {
  private static Gson GSON = new Gson();

  private Websocket gameSocket;
  private User u;

  public UserPlayer(User u, Websocket gameSocket) {
    super();
    this.u = u;
    this.gameSocket = gameSocket;
  }

  public UserPlayer(User u, Websocket gameSocket, UserGame g) {
    super(g);
    this.u = u;
    this.gameSocket = gameSocket;
  }

  //TODO perhaps this is not a great idea
  public UserGame getUserGame(){
    return (UserGame) getGame();
  }

  //TODO should these be public ??? probs not
  public PlayerWake wakeType = NONE;
  public int wakeData = 0;
  public List<Integer> wakeDataList = ImmutableList.of();

  @Override
  public synchronized int playHandAction() {
    try {
      while (wakeType != PLAY_ACTION) {
        wait();
      }
      wakeType = NONE;
      return wakeData;
    } catch (InterruptedException ignored) { }
    throw new RuntimeException("faulty play action");
  }

  @Override
  public synchronized List<Integer> buyCards() {
    try {
      while (wakeType != BUY_CARDS) {
        wait();
      }
      wakeType = NONE;
      return wakeDataList;
    } catch (InterruptedException ignored) { }
    throw new RuntimeException("faulty buy action");
  }

  @Override
  public synchronized int selectHand(List<Integer> cardIds, boolean
    cancelable, String
    name) {
    try {
      while (wakeType != PLAY_ACTION) {
        wait();
      }
      wakeType = NONE;
      return wakeData;
    } catch (InterruptedException ignored) { }
    return 0;
  }

  @Override
  public int selectBoard(List<Integer> cardIds, boolean cancelable, String name) {
    sendPlayerAction(new RequirePlayerAction(this, SELECT_BOARD, new Callback(
      cardIds, ImmutableList.of(), name, cancelable
    ), true));
    try {
      while (wakeType != SELECT_BOARD) {
        wait();
      }
      wakeType = NONE;
      return wakeData;
    } catch (InterruptedException ignored) { }
    return 0;
  }

  @Override
  public String selectButtons(List<String> buttonNames, String name) {
    return null;
  }


  // ========           OVERRIDE WITH SEND TO THE CLIENT     ========== //

  @Override
  public void draw(int numCards) {
    super.draw(numCards);
    sendHand();
  }

  public void adventurer() {
    super.adventurer();
    sendHand();
  }

  public void buyCard(Card boughtCard) {
    super.buyCard(boughtCard);
    sendDiscardSize();
  }

  public Card play(int posInHand) throws NoActionsException {
    Card c = super.play(posInHand);
    sendHand();
    return c;
  }

  public Card trash(int posInHand) {
    Card c = super.trash(posInHand);
    sendHand();
    return c;
  }

  public void gain(Card c, boolean toHand, boolean toDeck) {
    super.gain(c, toHand, toDeck);
    sendHand();
  }

  public void discard(int toDiscard) {
    super.discard(toDiscard);
    sendHand();
  }

  public void discardDeck() {
    super.discardDeck();
    sendDeckSize();
    sendDiscardSize();
  }

  public Card cardToDeck(int loc) {
    Card c = super.cardToDeck(loc);
    sendHand();
    return c;
  }

  public void endTurn() {
    super.endTurn();
    sendHand();
    //TODO possibly send the turn here or something
  }

  public void resetTurnValues() {
    super.resetTurnValues();
    sendMoney();
    sendActions();
    sendBuys();
  }

  public void newTurn() {
    super.newTurn();
    sendNotify("Your Turn");
    sendActions();
    sendBuys();
    sendPhase("action");
  }

  public void incrementActions() {
    super.incrementActions();
    sendActions();
  }

  public void incrementBuys() {
    super.incrementBuys();
    sendBuys();
  }

  public void decrementActions() {
    super.decrementActions();
    sendActions();
  }

  public void decrementBuys() {
    super.decrementBuys();
    sendBuys();
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    super.incrementAdditionalMoney(adnlMoney);
    sendMoney();
  }

  public void decrementAdditionalMoney(int adnlMoney) {
    super.decrementAdditionalMoney(adnlMoney);
    sendMoney();
  }


  @Override
  public void endActionPhase(){
    super.endActionPhase();
    sendHand();
    sendPhase("buy");
  }

  public void setActions(int a) {
    super.setActions(a);
    sendActions();
  }

  @Override
  public JsonObject toJson() {
    JsonObject main = new JsonObject();
    main.addProperty("id", getId());
    main.addProperty("color", u.getColor());
    main.addProperty("name", u.getName());
    return main;
  }

  private void sendHand() {
    List<Integer> ints = new ArrayList<>();
    for(Card c : getHand()){
      ints.add(c.getId());
    }
    if (u != null) {
      getUserGame().sendAllHandSize(this);
      gameSocket.send(u, HAND, GSON.toJson(ints));
    }
    sendMoney();
    sendDeckSize();
    sendDiscardSize();
  }

  private void sendDeckSize() {
    if (u != null) {
      gameSocket.send(u, DECK_SIZE, getDeck().size());
    }
  }

  private void sendDiscardSize() {
    if (u != null) {
      gameSocket.send(u, DISCARD_SIZE, getDiscard().size());
    }
  }

  private void sendMoney (){
    if (u != null) {
      System.out.println("$: " + getMoney());
      gameSocket.send(u, GOLD, getMoney());
    }
  }

  private void sendActions (){
    if (u != null) {
      gameSocket.send(u, ACTIONS, getActions());
    }
  }

  private void sendBuys (){
    if (u != null) {
      gameSocket.send(u, BUYS, getBuys());
    }
  }
  private void sendPlayerAction (RequirePlayerAction rpa) {
    List<JsonObject> requireActions = new ArrayList<>();
    requireActions.add(rpa.toJson());
    if (u != null) {
      gameSocket.send(u, PLAYER_ACTIONS, GSON.toJson(requireActions));
    }
  }

  private void sendPhase(String phase) {
    if (u != null) {
      gameSocket.send(u, PHASE, phase);
    }
  }

  private void sendNotify(String s) {
    gameSocket.send(u, NOTIFY, s);
  }

  public User getUser() {
    return u;
  }

  public void sendAll (Session s) {
    List<Integer> ints = new ArrayList<>();
    for(Card c : getHand()){
      ints.add(c.getId());
    }
    gameSocket.send(s, HAND, GSON.toJson(ints));
    gameSocket.send(s, BUYS, getBuys());
    gameSocket.send(s, ACTIONS, getActions());
    gameSocket.send(s, GOLD, getMoney());
    gameSocket.send(s, DISCARD_SIZE, getDiscard().size());
    gameSocket.send(s, DECK_SIZE, getDeck().size());
    gameSocket.send(s, PHASE, isActionPhase() ? "action" : "buy");
    getUserGame().sendBoard(s);
    getUserGame().sendTurn(s);
  }
}

