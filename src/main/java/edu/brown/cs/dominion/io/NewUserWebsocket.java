package edu.brown.cs.dominion.io;

import com.google.common.base.CharMatcher;
import org.eclipse.jetty.websocket.api.Session;

import static edu.brown.cs.dominion.io.send.MessageType.*;


/**
 * Websocket that allows users to change their names, and sends them to the
 * lobby
 * Created by henry on 5/6/2017.
 */
public class NewUserWebsocket implements SocketServer{
  // letters in an acceptable name
  private static final CharMatcher LETTERS = CharMatcher.ascii();

  @Override
  public void newUser(Websocket ws, User u) {}

  @Override
  public void newSession(Websocket ws, User u, Session s) {}

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(CHANGE_NAME,  (w, u, m) -> {
      //force their name into an acceptable format
      String name = LETTERS.retainFrom(m);
      if (name.length() > 20) { name = name.substring(0, 20); }
      name = name.trim();
      u.setName(name.length() == 0 ? "I am lame" : name);
      ws.send(u, REDIRECT, "lobby");
    });
  }
}
