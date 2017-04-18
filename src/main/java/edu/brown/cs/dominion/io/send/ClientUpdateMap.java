package edu.brown.cs.dominion.io.send;

import com.google.gson.Gson;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by henry on 3/22/2017.
 */
public class ClientUpdateMap {
  private transient static final Gson GSON = new Gson();
  private Map<String, Object> data;
  private Map<String, Object> dataGlobal;
  private transient SelectCallback callback;
  private transient User callbackUser;

  private transient List<User> users;

  public ClientUpdateMap(Game g) {
    data = new HashMap<>();
    dataGlobal = new HashMap<>();
    this.users = g.getAllUsers();

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

  public ClientUpdateMap requireSelect(User u,
                                       List<Card> handIds,
                                       List<Card> boardIds,
                                       SelectCallback response) {
    callback = response;
    callbackUser = u;
    data.put("select", true);
    data.put("handSelect", map(handIds, Card::getId));
    data.put("boardSelect", map(boardIds, Card::getId));
    return this;
  }

  public ClientUpdateMap hand(List<Card> cards) {
    data.put("hand", map(cards, Card::getId));
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

  public ClientUpdateMap turn(int userId){
    dataGlobal.put("userid", userId);
    return this;
  }


  public boolean hasUser(){
    return data.size() > 0;
  }

  public boolean hasGlobal(){
    return dataGlobal.size() > 0;
  }

  public String prepareUser() {
    return GSON.toJson(data);
  }

  public String prepareGlobal() { return GSON.toJson(dataGlobal); }

  public boolean hasCallback() {
    return callback != null;
  }

  public SelectCallback getCallback() {
    return callback;
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public User getCallbackUser() {
    return callbackUser;
  }

  public List<User> getUsers(){
    return users;
  }
}
