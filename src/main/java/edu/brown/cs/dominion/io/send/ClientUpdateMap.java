package edu.brown.cs.dominion.io.send;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.io.ButtonCallback;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 * Created by henry on 3/22/2017.
 */
public class ClientUpdateMap {
  static int nextButtonId = 1;
  private transient static final Gson GSON = new Gson();
  private Map<String, Object> data;
  private Map<String, Object> dataGlobal;
  private Multimap<User, ButtonCall> buttonCallbacks;
  private transient Map<User, Callback> callbacks;
  private transient User mainUser;
  private transient List<User> users;
  private Game g;

  public ClientUpdateMap(Game g, User u) {
    this.g = g;
    data = new HashMap<>();
    dataGlobal = new HashMap<>();
    this.users = g.getAllUsers();
    callbacks = new HashMap<>();
    mainUser = u;
    buttonCallbacks = HashMultimap.create();
  }

  public ClientUpdateMap setPhase(boolean action){
    data.put("phase", action ? "action" : "buy");
    return this;
  }

  public ClientUpdateMap actionCount(int actions) {
    data.put("actions", actions);
    return this;
  }

  public ClientUpdateMap buyCount(int buys) {
    data.put("buys", buys);
    return this;
  }

  public ClientUpdateMap goldCount(int gold) {
    data.put("gold", gold);
    return this;
  }

  public ClientUpdateMap requireSelect(User u, List<Integer> handIds,
                                       List<Integer> boardIds, SelectCallback
                                         response, String name) {
    callbacks.put(u, new Callback(boardIds, handIds, response, name));
    return this;
  }
  public ClientUpdateMap requireSelectCanStop(User u, List<Integer> handIds,
                                       List<Integer> boardIds, SelectCallback
                                                response, CancelHandler ch,
                                              String name) {
    callbacks.put(u, new Callback(boardIds, handIds, response, ch, name));
    return this;
  }

  public ClientUpdateMap putButton(User u, String s, ButtonCallback bc) {
    buttonCallbacks.put(u, new ButtonCall(s, bc));
    return this;
  }

  public ClientUpdateMap hand(List<Card> cards) {
    data.put("hand", map(cards, Card::getId));
    dataGlobal.put("handcardnum", new PlayerCards(mainUser.getId(), cards
      .size()));
    return this;
  }

  public ClientUpdateMap deckRemaining(int numCards) {
    data.put("decksize", numCards);
    return this;
  }

  public ClientUpdateMap discardPileSize(int numCards) {
    data.put("discardsize", numCards);
    return this;
  }

  public ClientUpdateMap piles(Board b) {
    dataGlobal.put("board", b);
    return this;
  }

  public ClientUpdateMap holdUntilInformed() {
    data.put("holding", true);
    return this;
  }

  public ClientUpdateMap finishSelect() {
    data.put("select", false);
    return this;
  }

  public ClientUpdateMap turn(int userId) {
    dataGlobal.put("turn", userId);
    return this;
  }

  public ClientUpdateMap winner(Map<Integer, Integer> idToVictoryPoints) {
    dataGlobal.put("winner", idToVictoryPoints);
    return this;
  }

  public boolean hasUser(User u) {
    if (callbacks.containsKey(u)) {
      return true;
    } if (buttonCallbacks.containsKey(u)) {
      return true;
    } if (mainUser == u && data.size() > 0) {
      return true;
    } if (dataGlobal.size() > 0) {
      return true;
    }
    return false;
  }

  public String prepareUser(User u) {
    if (u instanceof AIPlayer) {
      AIPlayer ai = (AIPlayer) u;
      Callback c = callbacks.containsKey(u) ? callbacks.get(u) : null;
      List<ButtonCall> bc = new LinkedList<>(buttonCallbacks.get(u));
      ((AIPlayer) u).doCallback(g, c, bc);
      return null;
    } else {
      Map<String, Object> toSend = new HashMap<>(dataGlobal);
      toSend.put("buttons", buttonCallbacks.get(u));
      if (mainUser == u) {
        toSend.putAll(data);
      } if (callbacks.containsKey(u)) {
        Callback c = callbacks.get(u);

        toSend.put("select", true);
        toSend.put("handSelect", c.getHandIds());
        toSend.put("boardSelect", c.getBoardIds());
        toSend.put("stoppable", c.isStoppable());
      }
      return GSON.toJson(toSend);
    }
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public User getMainUser() {
    return mainUser;
  }

  public List<User> getUsers() {
    return users;
  }

  public Map<User, Callback> getCallbacks(){
    return callbacks;
  }

  public Multimap<User, ButtonCall> getButtonCallbacks(){
    return buttonCallbacks;
  }
}

class PlayerCards{
  int id;
  int cards;
  public PlayerCards(int id, int cards){
    this.cards = cards;
    this.id = id;
  }
}
