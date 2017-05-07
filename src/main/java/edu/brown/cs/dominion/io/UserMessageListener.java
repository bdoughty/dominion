package edu.brown.cs.dominion.io;

/**
 * An interface to allow classes to subscribe to a message type on a websocket.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface UserMessageListener {
  void handleMessage(Websocket ws, User u, String messageData);
}
