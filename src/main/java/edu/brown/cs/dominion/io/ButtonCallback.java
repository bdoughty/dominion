package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

/**
 * Created by henry on 4/29/2017.
 */
@FunctionalInterface
public interface ButtonCallback {
  ClientUpdateMap clicked(User u);
}
