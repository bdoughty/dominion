package edu.brown.cs.dominion.io;

import com.google.common.base.CharMatcher;
import edu.brown.cs.dominion.User;
import org.eclipse.jetty.websocket.api.Session;

import static edu.brown.cs.dominion.io.send.MessageType.*;


/**
 * Created by henry on 5/6/2017.
 */
public class NewUserWebsocket implements SocketServer{
  private static final CharMatcher LETTERS = CharMatcher.ascii();

  @Override
  public void newUser(Websocket ws, User u) {}

  @Override
  public void newSession(Websocket ws, User u, Session s) {}

  @Override
  public void registerGlobalCommands(Websocket ws) {
    ws.putCommand(CHANGE_NAME,  (w, u, m) -> {
      String name = LETTERS.retainFrom(m);
      if (name.length() > 20) { name = name.substring(0, 20); }
      u.setName(name);
    });
  }
}
