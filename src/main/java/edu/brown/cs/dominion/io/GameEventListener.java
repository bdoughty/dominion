package edu.brown.cs.dominion.io;

import java.util.List;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

/**
 * Created by henry on 3/22/2017.
 */
public interface GameEventListener {
  void endBuyPhase(User u, List<Integer> toBuy);
  void doAction(User u, int locInHand);
  void endActionPhase(User u);
  ClientUpdateMap fullUpdate(User u);
  void startTurn(User u);
}
