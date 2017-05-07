package edu.brown.cs.dominion.io;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry of all the users in the system, associated by ID.
 *
 * This system is also in charge of constructing new users for new logons and
 * making sure that the IDs of the different users are pseudoRandom (hard to
 * predict) and do not collide.
 *
 * Created by henry on 3/28/2017.
 */
@WebSocket
public class UserRegistry{
  private Map<Integer, User> users;
  private int playerNumber = 0;
  private int nextId;

  /**
   * Constructor to set up the user registry.
   */
  public UserRegistry(){
    users = new HashMap<>();
    //so that no one can predict the
    nextId = (int) (Math.random() * 100000000);
  }

  /**
   * Create a new user and enter it into the registry.
   * @return the user that was constructed.
   */
  public User registerNewUser(){
    int id = getNewId();
    User u = new User(id, ++playerNumber);
    users.put(id, u);
    return u;
  }

  /**
   * Get a user from their id.
   * @param id the id of the user to be retrieved.
   * @return the User from the id.
   * @throws NoUserException if there is no user by the given ID.
   */
  public User getById(int id) throws NoUserException {
    if (users.containsKey(id)) {
      return users.get(id);
    } else {
      throw new NoUserException("User with id " + id + " does not exist");
    }
  }

  /**
   * check if there exists a user with a given id.
   * @param id the id to check.
   * @return boolean true if such a user exists, false otherwise.
   */
  public boolean hasUser(int id) {
    return users.containsKey(id);
  }


  /**
   * Get a new random-ish id for a new user.
   * @return the id.
   */
  private int getNewId(){
    nextId += 10 + (int) (Math.random() * 90);
    return nextId;
  }
}
