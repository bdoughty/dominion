package edu.brown.cs.dominion.games;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.dominion.io.User;
import edu.brown.cs.dominion.io.SocketServer;
import edu.brown.cs.dominion.io.UserRegistry;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.PlayerWake;
import edu.brown.cs.dominion.players.UserPlayer;
import org.eclipse.jetty.websocket.api.Session;

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
  private Map<User, UserPlayer> userPlayers;
  private Map<Integer, PendingGame> pendingGames;
  private Map<User, PendingGame> pendingByUser;

  public GameManager(UserRegistry users) {
    userPlayers = new HashMap<>();
    pendingGames = new HashMap<>();

    // TODO GET RID OF DUMMY
    PendingGame p = new PendingGame("Henry's game", 1,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 23 });
    pendingGames.put(p.getId(), p);

    PendingGame p2 = new PendingGame("Brendan's Game", 2,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 22 });
    pendingGames.put(p2.getId(), p2);

    PendingGame p3 = new PendingGame("Hank's Game", 3,
        new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 22 });
    pendingGames.put(p3.getId(), p3);
    pendingByUser = new HashMap<>();
  }

  /**
   * Set the websocket post construction.
   * @param web the websocket to set.
   */
  public void setWeb(Websocket web) {
    this.web = web;
  }

  private void action(User user, int cardLocation) {
    UserPlayer p = userPlayers.get(user);
    //notify the player if waiting on action with action.
    p.wake(PlayerWake.PLAY_ACTION, cardLocation);
  }

  private void endActionPhase(User user) {
    UserPlayer p = userPlayers.get(user);
    //notify the player if waiting on action with end action phase
    p.wake(PlayerWake.PLAY_ACTION, -1);
  }

  private void selection(User u, boolean inHand, int location, int id) {
    UserPlayer p = userPlayers.get(u);
    //notify the player of a selection.
    p.wake(PlayerWake.REQUEST_RESPONSE, location, id);
  }

  private void cancleSelect(User u) {
    UserPlayer p = userPlayers.get(u);
    //notify the player if waiting on action with cancel.
    p.wake(PlayerWake.CANCEL, -1);
  }

  private void button(User u, int id) {
    UserPlayer p = userPlayers.get(u);
    // notify the player if waiting on button press
    p.wake(PlayerWake.REQUEST_RESPONSE, id);
  }

  private void endBuy(User u, List<Integer> buys) {
    UserPlayer p = userPlayers.get(u);
    // notify the player if waiting on buys
    p.wake(PlayerWake.BUY_CARDS, buys);
  }

  @Override
  public void newUser(Websocket ws, User user) {
    ws.send(user, REDIRECT, "name");
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
      container.addProperty("time",
        p.getId() == g.getCurrentPlayerId() ? g.getTimeLeftOnTurn() - 1000 :
          60000);
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
      int id = data.get("id").getAsInt();
      int location = data.get("loc").getAsInt();
      selection(u, inHand, location, id);
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
      userPlayers.remove(u);
      web.send(u, REDIRECT, "lobby");
    });
  }

  public List<JsonObject> getPendingGames() {
    return pendingGames.values().stream().map(PendingGame::toJson).collect(Collectors.toList());
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

  public void removePendingByUser(List<User> users){
    users.forEach(u -> pendingByUser.remove(u));
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

  public void finish(List<User> allUsers) {
    allUsers.forEach(u -> userPlayers.remove(u));
    System.out.println("removing users");
  }
}
