package edu.brown.cs.dominion.games;

import static edu.brown.cs.dominion.io.send.MessageType.DO_ACTION;
import static edu.brown.cs.dominion.io.send.MessageType.END_ACTION;
import static edu.brown.cs.dominion.io.send.MessageType.END_BUY;
import static edu.brown.cs.dominion.io.send.MessageType.GLOBAL_UPDATE_MAP;
import static edu.brown.cs.dominion.io.send.MessageType.INIT_GAME;
import static edu.brown.cs.dominion.io.send.MessageType.SELECTION;
import static edu.brown.cs.dominion.io.send.MessageType.UPDATE_MAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jetty.websocket.api.Session;

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

/**
 * A wrapper that adds various ajax functionality to the server and then cleans
 * the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameManager implements SocketServer {
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

    // TODO GET RID OF DUMMY
    PendingGame p = new PendingGame("GAME1", 1,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    pendingGames.put(p.getId(), p);

    PendingGame p2 = new PendingGame("GAME2", 2,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    pendingGames.put(p2.getId(), p2);

    PendingGame p3 = new PendingGame("GAME3", 3,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    pendingGames.put(p3.getId(), p3);
    pendingByUser = new HashMap<>();
  }

  private Map<User, SelectCallback> callbacks;

  private ClientUpdateMap buys(User user, List<Integer> buys) {
    Game g = gamesByUser.get(user);
    return g.endBuyPhase(user, buys);
  }

  private ClientUpdateMap action(User user, int cardLocation) {
    Game g = gamesByUser.get(user);
    return g.doAction(user, cardLocation);
  }

  private ClientUpdateMap endActionPhase(User user) {
    Game g = gamesByUser.get(user);
    return g.endActionPhase(user);
  }

  private ClientUpdateMap selection(User u, boolean inHand, int location) {
    assert callbacks.containsKey(u);
    return callbacks.get(u).call(u, inHand, location);
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  private ClientUpdateMap chk(ClientUpdateMap c) {
    if (c != null && c.hasCallback()) {
      callbacks.put(c.getCallbackUser(), c.getCallback());
    }
    return c;
  }

  @Override
  public void newUser(Websocket ws, User user) {
    //redirect??
  }

  @Override
  public void newSession(Websocket ws, User user, Session s) {
    if (gamesByUser.containsKey(user)) {
      Game g = gamesByUser.get(user);
      List<Integer> actionIds = g.getBoard().getActionCardIds();

      JsonObject container = new JsonObject();
      container.addProperty("gameid", g.getId());
      container.add("cardids", GSON.toJsonTree(actionIds));
      container.add("users", GSON.toJsonTree(g.getAllUsers()));

      ws.send(s, INIT_GAME, GSON.toJson(container));
      ws.send(s, UPDATE_MAP, g.fullUpdate(user).prepareUser());
    } else {
      System.out.println("User is not in a game");
    }
  }

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(DO_ACTION, (w, u, m) -> {
      System.out.println(w + " - " + u + " - " + m);
      JsonObject data = PARSE.parse(m).getAsJsonObject();
      sendClientUpdateMap(ws, u, action(u, data.get("handloc").getAsInt()));
    });

    ws.putCommand(SELECTION, (w, u, m) -> {
      JsonObject data = PARSE.parse(m).getAsJsonObject();
      boolean inHand = data.get("inhand").getAsBoolean();
      int location = data.get("loc").getAsInt();
      sendClientUpdateMap(ws, u, selection(u, inHand, location));
    });

    ws.putCommand(END_ACTION,
      (w, u, m) -> sendClientUpdateMap(ws, u, endActionPhase(u)));

    ws.putCommand(END_BUY, (w, u, m) -> {
      JsonArray data = PARSE.parse(m).getAsJsonArray();
      List<Integer> buys = new LinkedList<>();
      for (JsonElement e : data) {
        buys.add(e.getAsInt());
      }
      sendClientUpdateMap(ws, u, buys(u, buys));
    });
  }

  public List<PendingGame> getPendingGames() {
    return new ArrayList<>(pendingGames.values());
  }

  public boolean joinGame(Websocket ws, User u, int id) {
    if (gamesByUser.containsKey(u) || pendingByUser.containsKey(u)) {
      System.out.println(
          "ERROR: user tried to join game but is already in a " + "game");
      return false;
    } else {
      if (pendingGames.containsKey(id)) {
        PendingGame pg = pendingGames.get(id);
        pendingByUser.put(u, pg);
        boolean didJoin = pg.addUser(u);
        if (pg.full()) {
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

  public void leave(User u) {
    if (pendingByUser.containsKey(u)) {
      PendingGame g = pendingByUser.remove(u);
      g.removeUser(u);
    }
  }

  public void sendClientUpdateMap(Websocket ws, User u, ClientUpdateMap c) {
    if (c != null) {
      c = chk(c);
      if (c.hasUser()) {
        ws.send(u, UPDATE_MAP, c.prepareUser());
      }
      if (c.hasGlobal()) {
        for (User user : c.getUsers()) {
          ws.send(user, GLOBAL_UPDATE_MAP, c.prepareGlobal());
        }
      }
    }
  }

  public void addPendingGame(PendingGame p){
    pendingGames.put(p.getId(), p);
  }
}
