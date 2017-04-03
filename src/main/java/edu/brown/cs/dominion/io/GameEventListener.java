package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.GameInit;

import java.util.List;

/**
 * Created by henry on 3/22/2017.
 */
public interface GameEventListener {
  ClientUpdateMap endBuyPhase(User u, List<Card> toBuy);
  ClientUpdateMap doAction(User u, Card action);
  ClientUpdateMap endActionPhase(User u);
  GameInit sendStartingInfo();
  ClientUpdateMap fullUpdate(User u);
}
