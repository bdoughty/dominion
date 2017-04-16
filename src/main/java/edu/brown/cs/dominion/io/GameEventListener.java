package edu.brown.cs.dominion.io;

import java.util.List;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.GameInit;

/**
 * Created by henry on 3/22/2017.
 */
public interface GameEventListener {
  ClientUpdateMap endBuyPhase(User u, List<Integer> toBuy);

  ClientUpdateMap doAction(User u, int locInHand);

  ClientUpdateMap endActionPhase(User u);

  GameInit sendStartingInfo();

  ClientUpdateMap fullUpdate(User u);
}
