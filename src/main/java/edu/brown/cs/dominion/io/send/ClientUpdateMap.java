package edu.brown.cs.dominion.io.send;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

import com.google.gson.JsonObject;
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
  private transient Map<User, List<RequirePlayerAction>> callbacks;
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

  public ClientUpdateMap requirePlayerAction(User u, RequirePlayerAction rpa) {
    if (!callbacks.containsKey(u)) {
      callbacks.put(u, new LinkedList<>());
    }
    callbacks.get(u).add(rpa);
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

  public ClientUpdateMap victoryPoints(Map<Integer, Integer> vps){
    dataGlobal.put("victorypoints", vps);
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
    return
      callbacks.containsKey(u) ||
      mainUser == u && data.size() > 0 ||
      dataGlobal.size() > 0;
  }

  public String prepareUser(User u) {
    if (u instanceof AIPlayer) {
      //TODO !!! reimplement AI
//      AIPlayer ai = (AIPlayer) u;
//      Callback c = callbacks.containsKey(u) ? callbacks.get(u) : null;
//      List<ButtonCall> bc = new LinkedList<>(buttonCallbacks.get(u));
//      ((AIPlayer) u).doCallback(g, c, bc);
      return null;
    } else {
      Map<String, Object> toSend = new HashMap<>(dataGlobal);
      if (mainUser == u) {
        toSend.putAll(data);
      } if (callbacks.containsKey(u)) {
        List<JsonObject> requireActions = new ArrayList<>();
        callbacks.get(u).forEach(ra -> requireActions.add(ra.toJson()));
        toSend.put("playeractions", requireActions);
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

  public Map<User, List<RequirePlayerAction>> getCallbacks(){
    return callbacks;
  }
}

class PlayerCards{
  private int id;
  private int cards;
  PlayerCards(int id, int cards){
    this.cards = cards;
    this.id = id;
  }
}
