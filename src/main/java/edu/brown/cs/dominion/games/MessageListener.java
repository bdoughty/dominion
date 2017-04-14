package edu.brown.cs.dominion.games;

import org.eclipse.jetty.websocket.api.Session;

/**
 * An interface to allow classes to pull different messages from the central
 * messaging system.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface MessageListener {
  void handleMessage(Session user, String messageData);
}
