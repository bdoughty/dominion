package edu.brown.cs.dominion.games;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.BigMoneyBigVictoryPoints;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.io.ButtonCallback;
import edu.brown.cs.dominion.io.send.ButtonCall;
import edu.brown.cs.dominion.io.send.Callback;
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
import edu.brown.cs.dominion.io.send.Callback;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * A wrapper that adds various ajax functionality to the server and then cleans
 * the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameManager implements SocketServer {
  private Websocket web;
  private static Gson GSON = new Gson();
  private static JsonParser PARSE = new JsonParser();

  private UserRegistry users;
  private Map<User, Game> gamesByUser;
  private List<Game> games;
  private Map<Integer, PendingGame> pendingGames;
  private Map<User, PendingGame> pendingByUser;

  public GameManager(UserRegistry users) {
    this.web = web;
    this.users = users;
    gamesByUser = new HashMap<>();
    games = new LinkedList<>();
    callbacks = new HashMap<>();
    pendingGames = new HashMap<>();
    buttonCallbacks = HashMultimap.create();

    // TODO GET RID OF DUMMY
    PendingGame p = new PendingGame("GAME1", 1,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    pendingGames.put(p.getId(), p);

    PendingGame p2 = new PendingGame("GAME2", 2,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    p2.addUser(users.registerNewAI(new BigMoneyBigVictoryPoints()));
    pendingGames.put(p2.getId(), p2);

    PendingGame p3 = new PendingGame("GAME3", 3,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    pendingGames.put(p3.getId(), p3);
    pendingByUser = new HashMap<>();
  }

  public void setWeb(Websocket web) {
    this.web = web;
  }

  private Map<User, Callback> callbacks;
  private Multimap<User, ButtonCall> buttonCallbacks;

  private void action(User user, int cardLocation) {
    Game g = gamesByUser.get(user);
    g.doAction(user, cardLocation);
  }

  private void endActionPhase(User user) {
    Game g = gamesByUser.get(user);
    g.endActionPhase(user);
  }

  private ClientUpdateMap selection(User u, boolean inHand, int location) {
    assert callbacks.containsKey(u);
    return callbacks.get(u).getCallback().call(u, inHand, location);
  }

  private ClientUpdateMap cancleSelect(User u) {
    assert callbacks.containsKey(u);
    Callback c = callbacks.get(u);
    assert c.isStoppable();
    return c.getCancelHandler().cancel();
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  private ClientUpdateMap chk(ClientUpdateMap c) {
    if (c != null) {
      callbacks.putAll(c.getCallbacks());
    }
    if (c != null){
      buttonCallbacks.putAll(c.getButtonCallbacks());
    }
    return c;
  }

  private ClientUpdateMap button(User u, int id) {
    List<ButtonCall> buttons = new LinkedList<>(buttonCallbacks.get(u));
    buttonCallbacks.removeAll(u);
    for (ButtonCall b : buttons) {
      if(b.getId() == id) {
        return b.getBc().clicked();
      }
    }
    return null;
  }

  @Override
  public void newUser(Websocket ws, User user) {
    // redirect??
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
      ws.send(s, UPDATE_MAP, g.fullUpdate(user).prepareUser(user));
    } else {
      System.out.println("User is not in a game");
    }
  }

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(DO_ACTION, (w, u, m) -> {
      System.out.println(w + " - " + u + " - " + m);
      JsonObject data = PARSE.parse(m).getAsJsonObject();
      action(u, data.get("handloc").getAsInt());
    });

    ws.putCommand(SELECTION, (w, u, m) -> {
      JsonObject data = PARSE.parse(m).getAsJsonObject();
      boolean inHand = data.get("inhand").getAsBoolean();
      int location = data.get("loc").getAsInt();
      sendClientUpdateMap(selection(u, inHand, location));
    });

    ws.putCommand(CANCEL_SELECT, (w, u, m) -> {
      sendClientUpdateMap(cancleSelect(u));
    });

    ws.putCommand(END_ACTION,
        (w, u, m) -> endActionPhase(u));

    ws.putCommand(END_BUY, (w, u, m) -> {
      JsonArray data = PARSE.parse(m).getAsJsonArray();
      List<Integer> buys = new LinkedList<>();
      for (JsonElement e : data) {
        buys.add(e.getAsInt());
      }
      Game g = gamesByUser.get(u);
      g.endBuyPhase(u, buys);
    });

    ws.putCommand(BUTTON_RESPONSE, (w, u, m) -> {
      int id = Integer.parseInt(m);
      button(u, id);
    });

    ws.putCommand(CHAT, (w, u, m) -> {
      if (gamesByUser.containsKey(u)) {
        Game g = gamesByUser.get(u);
        g.sendMessage(u, m);
      }
    });

    ws.putCommand(EXIT_GAME, (w, u, m) -> {
      Game g = gamesByUser.get(u);
      g.removeUser(u);
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
          Game g = pg.convertAndRedirect(ws, this);
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

  public void sendClientUpdateMap(ClientUpdateMap c) {
    if (c != null) {
      c = chk(c);
      for (User user : c.getUsers()) {
        if (c.hasUser(user)) {
          String s = c.prepareUser(user);
          if (s != null) {
            web.send(user, UPDATE_MAP, c.prepareUser(user));
          }
        }
      }
    }
  }

  public void addPendingGame(PendingGame p) {
    pendingGames.put(p.getId(), p);
  }

  public Websocket web() {
    return web;
  }
}
