package edu.brown.cs.dominion.games;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.SocketServer;
import edu.brown.cs.dominion.io.UserRegistry;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.SelectCallback;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * A wrapper that adds various ajax functionality to the server and then cleans
 * the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameManager implements SocketServer{
  private static Gson GSON = new Gson();
  private static JsonParser PARSE = new JsonParser();

  private UserRegistry users;
  private Map<User, Game> gamesByUser;
  private List<Game> games;
  private Map<Integer, PendingGame> pendingGames;

  public GameManager(UserRegistry users) {
    this.users = users;
    gamesByUser = new HashMap<>();
    games = new LinkedList<>();
    callbacks = new HashMap<>();
    pendingGames = new HashMap<>();
    PendingGame p = new PendingGame("JJ's secret tail", 4, new int[]{1,2,3,
      4,5,6,7,8,9}).addUser(new User(1)).addUser(new User(2));
    pendingGames.put(p.getId(), p);
  }

  private Map<User, SelectCallback> callbacks;

  private ClientUpdateMap buys(User user, List<Integer> buys) {
    Game g = gamesByUser.get(user);
    return chk(g.endBuyPhase(user, buys));
  }

  private ClientUpdateMap action(User user, int cardLocation) {
    Game g = gamesByUser.get(user);
    return chk(g.doAction(user, cardLocation));
  }

  private ClientUpdateMap endActionPhase(User user) {
    Game g = gamesByUser.get(user);
    return chk(g.endActionPhase(user));
  }

  private ClientUpdateMap selection(User u, boolean inHand, int location) {
    assert callbacks.containsKey(u);
    return chk(callbacks.get(u).call(inHand, location));
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  private ClientUpdateMap chk(ClientUpdateMap c) {
    if (c.hasCallback()) {
      callbacks.put(c.getCallbackUser(), c.getCallback());
    } return c;
  }

  @Override
  public void newUser(Websocket ws, User user) {
    ws.registerUserCommand(user, DO_ACTION,
      (w, u, m) -> {
        JsonObject data = PARSE.parse(m).getAsJsonObject();
        action(user, data.get("handloc").getAsInt());
      });

    ws.registerUserCommand(user, SELECTION,
      (w, u, m) -> {
        JsonObject data = PARSE.parse(m).getAsJsonObject();
        boolean inHand = data.get("inhand").getAsBoolean();
        int location = data.get("loc").getAsInt();
        selection(u, inHand, location);
      });

    ws.registerUserCommand(user, END_ACTION,
      (w, u, m) -> endActionPhase(u));

    ws.registerUserCommand(user, END_BUY,
      (w, u, m) -> {
        JsonArray data = PARSE.parse(m).getAsJsonArray();
        List<Integer> buys = new LinkedList<>();
        for(JsonElement e : data){
          buys.add(e.getAsInt());
        }
        buys(u, buys);
    });

  }

  @Override
  public void newSession(Websocket ws, User user, Session s) {

  }

  public List<PendingGame> getPendingGames() {
    return new ArrayList<>(pendingGames.values());
  }

  public boolean joinGame(User u, int id){
    if (gamesByUser.containsKey(u)) {
      System.out.println("ERROR: user tried to join game but is already in a " +
        "game");
      return false;
    } else {
      //TODO error catching
      pendingGames.get(id).addUser(u);
      return true;
    }
  }
}
