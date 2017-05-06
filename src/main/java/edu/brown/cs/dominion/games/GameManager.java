package edu.brown.cs.dominion.games;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.dominion.AI.Strategy.BigMoneyBigVictoryPoints;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.SocketServer;
import edu.brown.cs.dominion.io.UserRegistry;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.Callback;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.RequirePlayerAction;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.PlayerWake;
import edu.brown.cs.dominion.players.UserPlayer;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * A wrapper that adds various ajax functionality to the server and then cleans
 * the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameManager implements SocketServer {
  private static Gson GSON = new Gson();
  private static JsonParser PARSE = new JsonParser();

  private Websocket web;
  private UserRegistry users;
  private Map<User, UserPlayer> userPlayers;

  //private List<Game> games;

  private Map<Integer, PendingGame> pendingGames;
  private Map<User, PendingGame> pendingByUser;

  public GameManager(UserRegistry users) {
    this.users = users;
    userPlayers = new HashMap<>();
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

  public void setWeb(Websocket web) {
    this.web = web;
  }

  private void action(User user, int cardLocation) {
    UserPlayer p = userPlayers.get(user);
    synchronized (p) {
      p.wakeData = cardLocation;
      p.wakeType = PlayerWake.PLAY_ACTION;
      p.notifyAll();
    }
  }

  private void endActionPhase(User user) {
    UserPlayer p = userPlayers.get(user);
    synchronized (p) {
      p.wakeData = -1;
      p.wakeType = PlayerWake.PLAY_ACTION;
      p.notifyAll();
    }
  }

  private void selection(User u, boolean inHand, int location) {
    UserPlayer p = userPlayers.get(u);
    synchronized (p) {
      p.wakeData = location;
      if (inHand) {
        p.wakeType = inHand ? PlayerWake.SELECT_HAND : PlayerWake.SELECT_BOARD;
      }
      p.notifyAll();
    }
  }

  private void cancleSelect(User u) {
    UserPlayer p = userPlayers.get(u);
    synchronized (p) {
      p.wakeData = -1;
      p.wakeType = PlayerWake.CANCEL;
      p.notifyAll();
    }
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  private void button(User u, int id) {
    UserPlayer p = userPlayers.get(u);
    synchronized (p) {
      p.wakeData = id;
      p.wakeType = PlayerWake.PRESS_BUTTON;
      p.notifyAll();
    }
  }

  private void endBuy(User u, List<Integer> buys) {
    UserPlayer p = userPlayers.get(u);
    synchronized (p) {
      p.wakeDataList = buys;
      p.wakeType = PlayerWake.BUY_CARDS;
      p.notifyAll();
    }
  }

  @Override
  public void newUser(Websocket ws, User user) {
    // redirect??
  }

  @Override
  public void newSession(Websocket ws, User user, Session s) {
    if (userPlayers.containsKey(user)) {
      UserPlayer p =  userPlayers.get(user);
      Game g = p.getGame();

      List<Integer> actionIds = g.getBoard().getActionCardIds();
      JsonObject container = new JsonObject();
      container.addProperty("gameid", g.getId());
      container.add("cardids", GSON.toJsonTree(actionIds));
      container.add("users", GSON.toJsonTree(g.getPlayers().stream().map
        (Player::toJson).collect(Collectors.toList())));
      container.addProperty("id", userPlayers.get(user).getId());
      ws.send(s, INIT_GAME, GSON.toJson(container));
      p.sendAll(s);
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
      selection(u, inHand, location);
    });

    ws.putCommand(CANCEL_SELECT, (w, u, m) -> {
      cancleSelect(u);
    });

    ws.putCommand(END_ACTION,
        (w, u, m) -> endActionPhase(u));

    ws.putCommand(END_BUY, (w, u, m) -> {
      JsonArray data = PARSE.parse(m).getAsJsonArray();
      List<Integer> buys = new LinkedList<>();
      for (JsonElement e : data) {
        buys.add(e.getAsInt());
      }
      endBuy(u, buys);
    });

    ws.putCommand(BUTTON_RESPONSE, (w, u, m) -> {
      int id = Integer.parseInt(m);
      button(u, id);
    });

    ws.putCommand(CHAT, (w, u, m) -> {
      userPlayers.get(u).getUserGame().sendChat(u, m);
    });

    ws.putCommand(EXIT_GAME, (w, u, m) -> {
      UserPlayer p = userPlayers.get(u);
      Game g = p.getGame();
      g.removeUser(p);
      web.send(u, REDIRECT, "lobby");
    });
  }

  public List<PendingGame> getPendingGames() {
    return new ArrayList<>(pendingGames.values());
  }

  public boolean joinGame(Websocket ws, User u, int id) {
    if (userPlayers.containsKey(u) || pendingByUser.containsKey(u)) {
      System.out.println(
          "ERROR: user tried to join game but is already in a " + "game");
      return false;
    } else {
      if (pendingGames.containsKey(id)) {
        PendingGame pg = pendingGames.get(id);
        pendingByUser.put(u, pg);
        boolean didJoin = pg.addUser(u);
        if (pg.full()) {
          UserGame g = pg.convertAndRedirect(ws, this);
          userPlayers.putAll(g.getUserPlayerMap());
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

  public void addPendingGame(PendingGame p) {
    pendingGames.put(p.getId(), p);
  }

  public Websocket web() {
    return web;
  }
}
