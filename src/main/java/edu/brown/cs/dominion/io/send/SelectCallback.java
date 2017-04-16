package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 4/15/2017.
 */
@FunctionalInterface
public interface SelectCallback {
  /**
   * Callback for a select card message to the user
   * @param inHand card selected from hand or from board
   * @param loc the location of the card in the hand or on the board.
   * @return updateMap to the user.
   */
  ClientUpdateMap call(boolean inHand, int loc);
}
