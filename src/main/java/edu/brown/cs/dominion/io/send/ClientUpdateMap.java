package edu.brown.cs.dominion.io.send;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.Gson;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.Player;
import jdk.nashorn.internal.codegen.CompilerConstants;

import javax.jws.soap.SOAPBinding;

/**
 * Created by henry on 3/22/2017.
 */
public class ClientUpdateMap {
  private transient static final Gson GSON = new Gson();
  private Map<String, Object> data;
  private Map<String, Object> dataGlobal;
  private transient Map<User, Callback> callbacks;
  private transient User mainUser;
  private transient List<User> users;

  public ClientUpdateMap(Game g, User u) {
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

  public ClientUpdateMap requireSelect(User u, List<Integer> handIds,
      List<Integer> boardIds, SelectCallback response) {
    callbacks.put(u, new Callback(boardIds, handIds, response));
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

  public ClientUpdateMap winner(List<User> u) {
    dataGlobal.put("winner", u);
    return this;
  }

  public boolean hasUser(User u) {
    if (callbacks.containsKey(u)) {
      return true;
    } if (mainUser == u && data.size() > 0) {
      return true;
    } if (dataGlobal.size() > 0) {
      return true;
    }
    return false;
  }

  public String prepareUser(User u) {
    Map<String, Object> toSend = new HashMap<>(dataGlobal);
    if (mainUser == u) {
      toSend.putAll(data);
    } if (callbacks.containsKey(u)) {
      Callback c = callbacks.get(u);

      toSend.put("select", true);
      toSend.put("handSelect", c.getHandIds());
      toSend.put("boardSelect", c.getBoardIds());
    }
    return GSON.toJson(toSend);
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
}

class PlayerCards{
  int id;
  int cards;
  public PlayerCards(int id, int cards){
    this.cards = cards;
    this.id = id;
  }
}
