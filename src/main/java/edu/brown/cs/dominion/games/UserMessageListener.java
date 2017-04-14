package edu.brown.cs.dominion.games;

import edu.brown.cs.dominion.User;

/**
 * An interface to allow classes to pull different messages from the central
 * messaging system.
 * Created by henry on 4/14/2017.
 */
@FunctionalInterface
public interface UserMessageListener {
  void handleMessage(User u, String messageData);
}
