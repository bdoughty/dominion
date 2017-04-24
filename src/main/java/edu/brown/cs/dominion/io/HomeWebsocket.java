package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.dominion.Chat;
import edu.brown.cs.dominion.User;
import static edu.brown.cs.dominion.io.send.MessageType.*;

import edu.brown.cs.dominion.games.GameManager;
import edu.brown.cs.dominion.games.PendingGame;
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
  public void newUser(Websocket ws, User user) {
    //NOTHING
  }

  @Override
  public void newSession(Websocket ws, User u, Session s) {
    ws.send(s, PENDING_GAMES, GSON.toJson(gm.getPendingGames()));
  }

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(CHAT, this);
    ws.putCommand(JOIN_GAME,
      (w, u, m) -> {
        System.out.println(m);
        JsonObject o = PARSE.parse(m).getAsJsonObject();
        int gameid = o.get("gameid").getAsInt();
        boolean joined = gm.joinGame(ws, u, gameid);
        JsonObject message = new JsonObject();
        message.addProperty("gameid", gameid);
        message.addProperty("didjoin", joined);
        w.send(u, JOIN_RESPONSE, GSON.toJson(message));
        sendAllUpdateGames(w);
      });
    ws.putCommand(LEAVE,
      (w, u, m) -> {
        gm.leave(u);
        w.send(u, LEAVE_RESPONSE, "");
        sendAllUpdateGames(w);
      });
    ws.putCommand(NEW_GAME,
      (w, u, m) -> {
        JsonObject data = PARSE.parse(m).getAsJsonObject();
        String name = data.get("name").getAsString();
        JsonArray cards = data.get("cards").getAsJsonArray();
        int numPlayers = data.get("numPlayers").getAsInt();
        int[] crds = new int[10];
        for (int i = 0; i < 10; i++) {
          crds[i] = cards.get(i).getAsInt();
        }
        PendingGame p = new PendingGame(name, numPlayers, crds);
        w.send(u, REDIRECT, "lobby");
        gm.addPendingGame(p);
      }
    );
  }

  @Override
  public void handleMessage(Websocket ws, User u, String messageData) {
    if(messageData.startsWith("sudoall ")){
      ws.sendAllRaw(messageData.substring(8));
    }
    ws.sendAll(CHAT, homechat.getMessage(u.getName(), u.getColor(), messageData));
  }

  private void sendAllUpdateGames(Websocket ws){
    ws.sendAll(PENDING_GAMES, GSON.toJson(gm.getPendingGames()));
  }
}
