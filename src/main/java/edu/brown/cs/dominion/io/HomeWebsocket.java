package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.Chat;
import edu.brown.cs.dominion.User;
import static edu.brown.cs.dominion.io.send.MessageType.*;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Websocket for the home page
 * Created by henry on 4/14/2017.
 */
public class HomeWebsocket implements SocketServer, UserMessageListener {
  private Chat homechat;

  public HomeWebsocket(){
    homechat = new Chat();
  }

  @Override
  public void newUser(Websocket ws, User u) {
    ws.registerUserCommand(u, CHAT, this);
  }

  @Override
  public void newSession(Websocket ws, User u, Session s) {
    //DO Nothing
  }

  @Override
  public void handleMessage(Websocket ws, User u, String messageData) {
    ws.sendAll(CHAT, homechat.getMessage(u.getName(), u.getColor(), messageData));
  }
}
