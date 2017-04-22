package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.User;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by henry on 4/14/2017.
 */
public interface SocketServer {
  void newUser(Websocket ws, User u);
  void newSession(Websocket ws, User u, Session s);
  void registerGlobalCommands(Websocket ws);
}
