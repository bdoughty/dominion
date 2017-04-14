package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.io.Websocket;
import org.eclipse.jetty.websocket.api.Session;

/**
 * An interface to allow classes to pull different messages from the central
 * messaging system.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface MessageListener {
  void handleMessage(Websocket ws, Session user, String messageData);
}
