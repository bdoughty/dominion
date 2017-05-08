package edu.brown.cs.dominion.io.send;

import edu.brown.cs.dominion.players.UserInteruptedException;

/**
 * A function that allows the botton to provide backend functionality when
 * pressed.
 * Created by henry on 5/6/2017.
 */
@FunctionalInterface
public interface ButtonCallback {
  /**
   * Do what needs to be done when the button is pressed.
   */
  void pressed() throws UserInteruptedException;
}
