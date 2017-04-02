package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.user.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by henry on 3/22/2017.
 */
public class ClientUpdateMap implements Jsonable{
  private transient static final Gson GSON = new Gson();
  private Map<String, Object> data;
  private transient Function<Card, ClientUpdateMap> callback;

  public ClientUpdateMap() {
    data = new HashMap<>();
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

  public ClientUpdateMap requireSelect(List<Card> handIds,
                                       List<Card> boardIds,
                                       Consumer<Card> response) {
    callback = (c -> {response.accept(c); return new ClientUpdateMap();});
    data.put("select", true);
    data.put("handSelect", map(handIds, Card::getId));
    data.put("boardSelect", map(boardIds, Card::getId));
    return this;
  }

  public ClientUpdateMap requireSelect(List<Card> handIds,
                                       List<Card> boardIds,
                                       Function<Card, ClientUpdateMap> response) {
    callback = response;
    data.put("select", true);
    data.put("handSelect", map(handIds, Card::getId));
    data.put("boardSelect", map(boardIds, Card::getId));
    return this;
  }

  public ClientUpdateMap finishSelect() {
    data.put("select", false);
    return this;
  }

  public String prepare() {
    return GSON.toJson(data);
  }

  public boolean hasCallback() {
    return callback != null;
  }

  public Function<Card, ClientUpdateMap> getCallback() {
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
}
