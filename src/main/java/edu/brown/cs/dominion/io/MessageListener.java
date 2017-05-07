package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.io.Websocket;
import org.eclipse.jetty.websocket.api.Session;

/**
 * An interface to allow classes to subscribe to messages from a websocket
 * from a session that is not necessarily associated with a user yet.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface MessageListener {
  void handleMessage(Websocket ws, Session user, String messageData);
}
