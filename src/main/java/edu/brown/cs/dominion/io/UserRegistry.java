package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.Strategy;
import edu.brown.cs.dominion.User;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * A way that hooks into the WebSocket system in order to keep track of all
 * the different users.
 * Created by henry on 3/28/2017.
 */
@WebSocket
public class UserRegistry{
  private Map<Integer, User> users;
  private int startId;
  private int nextId;

  public UserRegistry(){
    users = new HashMap<>();
    startId = (int) (Math.random() * 100000000);
    nextId = startId;
  }

  public User registerNewUser(){
    User u = new User(nextId, startId);
    users.put(nextId, u);
    nextId++;
    return u;
  }
  public AIPlayer registerNewAI(Strategy s){
    AIPlayer u = new AIPlayer(nextId, startId, s);
    users.put(nextId, u);
    nextId++;
    return u;
  }

  public User getById(int id) {
    if (users.containsKey(id)) {
      return users.get(id);
    } else {
      System.out.println("Attempted to access nonexistent user");
      return null;
    }
  }

  public boolean hasUser(int id) {
    return users.containsKey(id);
  }


}
