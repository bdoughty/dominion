package edu.brown.cs.dominion.io;

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
  private int nextId = 0;

  public UserRegistry(){
    users = new HashMap<>();
  }

  public User registerNewUser(){
    User u = new User(nextId);
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
