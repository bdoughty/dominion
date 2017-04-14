package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.Websocket;

/**
 * An interface to allow classes to pull different messages from the central
 * messaging system.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface UserMessageListener {
  void handleMessage(Websocket ws, User u, String messageData);
}
