package edu.brown.cs.dominion.players;

import static edu.brown.cs.dominion.io.send.MessageType.ACTIONS;
import static edu.brown.cs.dominion.io.send.MessageType.BUYS;
import static edu.brown.cs.dominion.io.send.MessageType.DECK_SIZE;
import static edu.brown.cs.dominion.io.send.MessageType.DISCARD_SIZE;
import static edu.brown.cs.dominion.io.send.MessageType.GOLD;
import static edu.brown.cs.dominion.io.send.MessageType.HAND;
import static edu.brown.cs.dominion.io.send.MessageType.NOTIFY;
import static edu.brown.cs.dominion.io.send.MessageType.PHASE;
import static edu.brown.cs.dominion.io.send.MessageType.PLAYER_ACTIONS;
import static edu.brown.cs.dominion.players.PlayerWake.BUY_CARDS;
import static edu.brown.cs.dominion.players.PlayerWake.CANCEL;
import static edu.brown.cs.dominion.players.PlayerWake.NONE;
import static edu.brown.cs.dominion.players.PlayerWake.PLAY_ACTION;
import static edu.brown.cs.dominion.players.PlayerWake.REQUEST_RESPONSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.brown.cs.dominion.io.send.ButtonCallback;
import org.eclipse.jetty.websocket.api.Session;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.games.UserGame;
import edu.brown.cs.dominion.gameutil.EmptyDeckException;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.io.User;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.Button;
import edu.brown.cs.dominion.io.send.Callback;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;

/**
 * A player which is implemented across the network
 *
 * Automatically updates the client through pseudo-decorator pattern on super
 * class.
 *
 * Created by henry on 5/5/2017.
 */
public class UserPlayer extends Player {
  private static Gson GSON = new Gson();
  private List<String> userActions;

  private LazySender lazy;
  private Websocket gameSocket;
  private User u;

  public UserPlayer(User u, Websocket gameSocket) {
    super();
    this.u = u;
    this.gameSocket = gameSocket;
    userActions = new ArrayList<>();
    lazy = new LazySender();
  }

  public UserPlayer(User u, Websocket gameSocket, UserGame g) {
    super(g);
    this.u = u;
    this.gameSocket = gameSocket;
  }

  /**
   * the game of all UserPlayers must be a UserGame, and therefore it has the
   * ability to get its game as a UserGame.
   *
   * @return the UserGame that this player is a part of.
   */
  public UserGame getUserGame() {
    return (UserGame) getGame();
  }

  public synchronized void wake(PlayerWake wakeType, int wakeData) {
    this.wakeData = wakeData;
    this.wakeType = wakeType;
    notifyAll();
  }

  public synchronized void wake(PlayerWake wakeType, List<Integer> wakeData) {
    this.wakeType = wakeType;
    this.wakeDataList = wakeData;
    notifyAll();
  }

  public synchronized void wake(PlayerWake wakeType, int wakeData,
      int requestId) {
    this.wakeRequestID = requestId;
    wake(wakeType, wakeData);
  }

  // waking data to only trigger specific threads.
  private PlayerWake wakeType = NONE;
  private int wakeData = 0;
  private int wakeRequestID = 0;
  private List<Integer> wakeDataList = ImmutableList.of();

  @Override
  public synchronized int playHandAction() throws UserInteruptedException {
    try {
      while (wakeType != PLAY_ACTION) {
        wait();
        if (wakeType == NONE) {
          System.out.println("escaped handaction");
          throw new UserInteruptedException();
        }
      }
      wakeType = NONE;
      return wakeData;
    } catch (InterruptedException ignored) {
    }
    throw new RuntimeException("faulty play action");
  }

  @Override
  public synchronized List<Integer> buyCards() throws UserInteruptedException {
    try {
      while (wakeType != BUY_CARDS) {
        wait();
        if (wakeType == NONE) {
          System.out.println("buy");
          throw new UserInteruptedException();
        }
      }
      wakeType = NONE;
      return wakeDataList;
    } catch (InterruptedException ignored) {
    }
    throw new RuntimeException("faulty buy action");
  }

  @Override
  public synchronized int selectHand(List<Integer> cardIds, boolean cancelable,
      String name) throws UserInteruptedException {
    int requestId = RequirePlayerAction.nextId++;
    Finisher f = sendPlayerAction(new RequirePlayerAction(
        new Callback(ImmutableList.of(), cardIds, name, cancelable), true,
        requestId));
    System.out.println("NEW SELECT HAND: " + requestId);
    userActions.forEach(System.out::println);
    try {
      do {
        wait();
        System.out.println("wakeup");
        if (wakeType == CANCEL && cancelable) {
          f.finish();
          return -1;
        }
        if (wakeType == NONE) {
          System.out.println("escaped selectHand");
          f.finish();
          throw new UserInteruptedException();
        }
      } while (wakeType != REQUEST_RESPONSE && wakeRequestID == requestId);

      wakeType = NONE;
      wakeRequestID = -1;
      f.finish();
      return wakeData;
    } catch (InterruptedException ignored) {
      f.finish();
    }
    return -2;
  }

  @Override
  public synchronized int selectBoard(List<Integer> cardIds, boolean cancelable,
      String name) throws UserInteruptedException {
    int requestId = RequirePlayerAction.nextId++;
    Finisher f = sendPlayerAction(new RequirePlayerAction(
        new Callback(cardIds, ImmutableList.of(), name, cancelable), true,
        requestId));
    try {
      do {
        wait();
        if (wakeType == CANCEL && cancelable) {
          f.finish();
          return -1;
        }
        if (wakeType == NONE) {
          System.out.println("escaped selectboard");
          f.finish();
          throw new UserInteruptedException();
        }
      } while (wakeType != REQUEST_RESPONSE && requestId == wakeRequestID);
      wakeType = NONE;
      wakeRequestID = -1;
      f.finish();
      return wakeData;
    } catch (InterruptedException ignored) {
    }
    return -2;
  }

  public void lazySend(ButtonCallback bc) throws UserInteruptedException{
    lazy.sendLazyHand(this, bc);
  }

  @Override
  public Button selectButtons(String name, Button... buttons)
      throws UserInteruptedException {
    int requestId = RequirePlayerAction.nextId++;
    Finisher f = sendPlayerAction(
        new RequirePlayerAction(Arrays.asList(buttons), true, requestId));
    try {
      synchronized (this) {
        while (wakeType != REQUEST_RESPONSE) {
          wait();
          if (wakeType == NONE) {
            System.out.println("escaped selectButtons");
            throw new UserInteruptedException();
          }
          for (Button b : buttons) {
            if (b.getId() == wakeData) {
              f.finish();
              wakeType = NONE;
              return b;
            }
          }
        }
      }
    } catch (InterruptedException ignored) {
      f.finish();
    }
    System.out.println("ERROR: button id is incorrect");
    return null;
  }

  // ======== OVERRIDE WITH SEND TO THE CLIENT ========== //

  @Override
  public void draw(int numCards) {
    super.draw(numCards);
    sendHand();
  }

  public Card drawOne() throws EmptyDeckException {
    Card c = super.drawOne();
    sendHand();
    return c;
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
    sendActions();
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
  public void endActionPhase() {
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

  @Override
  public String getName() {
    return u.getName();
  }

  private void sendHand() {
    List<Integer> ints = new ArrayList<>();
    for (Card c : getHand()) {
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

  private void sendMoney() {
    if (u != null) {
      gameSocket.send(u, GOLD, getMoney());
    }
  }

  private void sendActions() {
    if (u != null) {
      gameSocket.send(u, ACTIONS, getActions());
    }
  }

  private void sendBuys() {
    if (u != null) {
      gameSocket.send(u, BUYS, getBuys());
    }
  }

  private Finisher sendPlayerAction(RequirePlayerAction rpa) {
    List<JsonObject> requireActions = new ArrayList<>();
    requireActions.add(rpa.toJson());
    if (u != null) {
      String s = GSON.toJson(requireActions);
      userActions.add(s);
      gameSocket.send(u, PLAYER_ACTIONS, s);
      return () -> {
        userActions.remove(s);
        if(userActions.size() == 0){
          synchronized (lazy) {
            lazy.notifyAll();
          }
        }
      };
    }
    return () -> {
    };
  }

  private void sendPhase(String phase) {
    if (u != null) {
      gameSocket.send(u, PHASE, phase);
    }
  }

  public void sendNotify(String s) {
    gameSocket.send(u, NOTIFY, s);
  }

  public User getUser() {
    return u;
  }

  public void sendAll(Session s) {
    List<Integer> ints = new ArrayList<>();
    for (Card c : getHand()) {
      ints.add(c.getId());
    }
    gameSocket.send(s, HAND, GSON.toJson(ints));
    gameSocket.send(s, BUYS, getBuys());
    gameSocket.send(s, ACTIONS, getActions());
    gameSocket.send(s, GOLD, getMoney());
    gameSocket.send(s, DISCARD_SIZE, getDiscard().size());
    gameSocket.send(s, DECK_SIZE, getDeck().size());
    gameSocket.send(s, PHASE, isActionPhase() ? "action" : "buy");
    userActions.forEach(a -> gameSocket.send(s, PLAYER_ACTIONS, a));
    getUserGame().sendBoard(s);
    getUserGame().sendTurn(s);
  }

  public boolean hasUserActions(){
    return !userActions.isEmpty();
  }

  public synchronized void cancelAll(){
    wakeType = NONE;
    notifyAll();
  }
}

@FunctionalInterface
interface Finisher {
  void finish();
}
