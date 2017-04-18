package edu.brown.cs.dominion.games;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
  private Map<User, PendingGame> pendingByUser;

  public GameManager(UserRegistry users) {
    this.users = users;
    gamesByUser = new HashMap<>();
    games = new LinkedList<>();
    callbacks = new HashMap<>();
    pendingGames = new HashMap<>();

    //TODO GET RID OF DUMMY
    PendingGame p = new PendingGame("JJ's secret tail", 1, new int[]{7,8,9,
      10,11,12,13,14,15,16});
    pendingGames.put(p.getId(), p);

    PendingGame p2 = new PendingGame("JJ's secret tail", 2, new int[]{7,8,9,
      10,11,12,13,14,15,16});
    pendingGames.put(p2.getId(), p2);

    PendingGame p3 = new PendingGame("JJ's secret tail", 3, new int[]{7,8,9,
      10,11,12,13,14,15,16});
    pendingGames.put(p3.getId(), p3);
    pendingByUser = new HashMap<>();
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
    if(gamesByUser.containsKey(user)){
      Game g = gamesByUser.get(user);
      List<Integer> actionIds = g.getBoard().getActionCardIds();


      JsonObject container = new JsonObject();
      container.addProperty("gameid", g.getId());
      container.add("cardids", GSON.toJsonTree(actionIds));
      List<User> us = new LinkedList<>(g.getUsers());
      us.add(0, g.getCurrent());
      container.add("users", GSON.toJsonTree(us));

      ws.send(s, INIT_GAME, GSON.toJson(container));
    } else {
      System.out.println("User is not in a game");
    }
    ws.send(s, INIT_GAME, "");
  }

  public List<PendingGame> getPendingGames() {
    return new ArrayList<>(pendingGames.values());
  }

  public boolean joinGame(Websocket ws, User u, int id){
    if (gamesByUser.containsKey(u) || pendingByUser.containsKey(u)) {
      System.out.println("ERROR: user tried to join game but is already in a " +
        "game");
      return false;
    } else {
      if(pendingGames.containsKey(id)){
        PendingGame pg = pendingGames.get(id);
        pendingByUser.put(u, pg);
        boolean didJoin = pg.addUser(u);
        if(pg.full()){
          Game g = pg.convertAndRedirect(ws);
          games.add(g);
          pg.getUsers().forEach(us -> gamesByUser.put(us, g));
          pg.getUsers().forEach(us -> pendingByUser.remove(us));
          pendingGames.remove(id);
        }
        return didJoin;
      } else {
        System.out.println("ERROR: no game by that id exists");
        return false;
      }
    }
  }

  public void leave(User u){
    if(pendingByUser.containsKey(u)){
      PendingGame g = pendingByUser.remove(u);
      g.removeUser(u);
    }
  }
}
