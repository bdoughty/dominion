package edu.brown.cs.dominion.games;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.Chat;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.GameEventListener;
import edu.brown.cs.dominion.io.UserSet;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.GameInit;
import edu.brown.cs.dominion.io.send.MessageType;

import java.util.List;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends UserSet implements GameEventListener{
  private Chat gameChat;

  public Game(List<User> usersTurns) {
    super(usersTurns);
    this.gameChat = new Chat();
  }

  @Override
  public ClientUpdateMap endBuyPhase(User u, List<Card> toBuy) {
    //TODO
    return null;

  }

  @Override
  public ClientUpdateMap doAction(User u, Card action) {
    //TODO
    return null;
  }

  @Override
  public ClientUpdateMap endActionPhase(User u) {
    //TODO
    return null;
  }

  @Override
  public GameInit sendStartingInfo() {
    //TODO
    return null;
  }

  @Override
  public ClientUpdateMap fullUpdate(User u) {
    //TODO
    return null;
  }


}
