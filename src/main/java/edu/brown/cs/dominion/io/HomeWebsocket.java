package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.dominion.Chat;
import edu.brown.cs.dominion.User;
import static edu.brown.cs.dominion.io.send.MessageType.*;

import edu.brown.cs.dominion.games.GameManager;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Websocket for the home page
 * Created by henry on 4/14/2017.
 */
public class HomeWebsocket implements SocketServer, UserMessageListener {
  private static Gson GSON = new Gson();
  private static JsonParser PARSE = new JsonParser();
  private Chat homechat;
  private GameManager gm;

  public HomeWebsocket(GameManager gm){
    this.gm = gm;
    homechat = new Chat();
  }

  @Override
  public void newUser(Websocket ws, User u) {
    ws.registerUserCommand(u, CHAT, this);
    ws.registerUserCommand(u, JOIN_GAME,
      (w, u, m) -> {
        JsonObject o = PARSE.parse(m).getAsJsonObject();
        int gameid = o.get("gameid").getAsInt();
        boolean joined = gm.joinGame(u, gameid);
        JsonObject message = new JsonObject();
        message.addProperty("gameid", gameid);
        message.addProperty("didjoin", joined);
        w.send(u, JOIN_RESPONSE, GSON.toJson(message));
      });
  }

  @Override
  public void newSession(Websocket ws, User u, Session s) {
    ws.send(s, PENDING_GAMES, GSON.toJson(gm.getPendingGames()));
  }

  @Override
  public void handleMessage(Websocket ws, User u, String messageData) {
    ws.sendAll(CHAT, homechat.getMessage(u.getName(), u.getColor(), messageData));
  }
}
