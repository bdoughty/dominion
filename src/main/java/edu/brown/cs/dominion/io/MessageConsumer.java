package edu.brown.cs.dominion.io;

import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by henry on 4/2/2017.
 */
@FunctionalInterface
public interface MessageConsumer {
  void accept(Session send, String Message);
}
