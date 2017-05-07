package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.brown.cs.dominion.Chat;

import static edu.brown.cs.dominion.io.send.MessageType.*;

import edu.brown.cs.dominion.games.GameManager;
import edu.brown.cs.dominion.games.PendingGame;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Websocket for the home page and the game creation page.
 *
 * Handles pending game messages, lobby chat and the create game page.
 * Created by henry on 4/14/2017.
 */
public class HomeWebsocket implements SocketServer, UserMessageListener {
  private static Gson GSON = new Gson();
  private static JsonParser PARSE = new JsonParser();
  private Chat homechat;
  private GameManager gm;

  /**
   * construct a new home websocket with a reference to the game manager for
   * pending games.
   *
   * Set up the lobby chat system.
   * @param gm the game manager.
   */
  public HomeWebsocket(GameManager gm){
    this.gm = gm;
    homechat = new Chat();
  }

  @Override
  public void newUser(Websocket ws, User user) {
    // if a new user is registered on this page, send them to the name
    // setting page to select a name.
    ws.send(user, REDIRECT, "name");
  }

  @Override
  public void newSession(Websocket ws, User u, Session s) {
    // when a user logs on give them information about the current pending games
    ws.send(s, PENDING_GAMES, GSON.toJson(gm.getPendingGames()));
  }

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(CHAT, this);
    ws.putCommand(JOIN_GAME,
      (w, u, m) -> {
        try {
          JsonObject o = PARSE.parse(m).getAsJsonObject();
          int gameid = o.get("gameid").getAsInt();
          boolean joined = gm.joinGame(ws, u, gameid);
          JsonObject message = new JsonObject();
          message.addProperty("gameid", gameid);
          message.addProperty("didjoin", joined);
          w.send(u, JOIN_RESPONSE, GSON.toJson(message));
          sendAllUpdateGames(w);
        } catch (JsonSyntaxException jse) {
          System.out.println("malformed message: " + m);
        }
      });
    ws.putCommand(LEAVE,
      (w, u, m) -> {
        gm.leave(u);
        w.send(u, LEAVE_RESPONSE, "");
        sendAllUpdateGames(w);
      });
    ws.putCommand(NEW_GAME,
      (w, u, m) -> {
        try {
          JsonObject data = PARSE.parse(m).getAsJsonObject();
          String name = data.get("name").getAsString();
          JsonArray cards = data.get("cards").getAsJsonArray();
          JsonArray ais = data.get("ais").getAsJsonArray();
          int numPlayers = data.get("numPlayers").getAsInt();
          int[] crds = new int[10];
          for (int i = 0; i < 10; i++) {
            crds[i] = cards.get(i).getAsInt();
          }
          List<String> aiTypes = new ArrayList<>();
          for (int i = 0; i < ais.size(); i++) {
            aiTypes.add(ais.get(i).getAsString());
          }
          PendingGame p = new PendingGame(name, numPlayers, crds, aiTypes);
          w.send(u, REDIRECT, "lobby");
          gm.addPendingGame(p);
          sendAllUpdateGames(w);
        } catch (JsonSyntaxException jse) {
          System.out.println("malformed message: " + m);
        }
      }
    );
  }

  @Override
  public void handleMessage(Websocket ws, User u, String messageData) {
    //upon a chat message send everyone that message
    ws.sendAll(CHAT, homechat.getMessage(u.getName(), u.getColor(), messageData));
  }

  private void sendAllUpdateGames(Websocket ws){
    ws.sendAll(PENDING_GAMES, GSON.toJson(gm.getPendingGames()));
  }
}
