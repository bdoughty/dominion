package edu.brown.cs.dominion.io;

import org.eclipse.jetty.websocket.api.Session;

/**
 * A server that runs a websocket.
 * Created by henry on 4/14/2017.
 */
public interface SocketServer {
  /**
   * Called whenever a new user is registered on this specific websocket.
   * @param ws the websocket.
   * @param u the user registered.
   */
  void newUser(Websocket ws, User u);

  /**
   * When a new session is loaded on a websocket.
   *
   * Often used to send any information pertinent to the page itself.
   * @param ws the websocket.
   * @param u the user associated with the new session, either a new user if
   *          the session did not previously have a User, or an existing user.
   * @param s the session that is newly loaded onto the websocket.
   */
  void newSession(Websocket ws, User u, Session s);

  /**
   * When the websocket is started this gives the socket server an
   * opportunity to subscribe to messages from the server.
   * @param ws the websocket.
   */
  void registerGlobalCommands(Websocket ws);
}
