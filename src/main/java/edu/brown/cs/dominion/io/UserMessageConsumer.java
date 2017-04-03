package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.User;

/**
 * Created by henry on 4/2/2017.
 */

@FunctionalInterface
public interface UserMessageConsumer{
  void accept(User u, String Message);
}
