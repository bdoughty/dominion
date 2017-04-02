package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * A way that hooks into the WebSocket system in order to keep track of all
 * the different users.
 * Created by henry on 3/28/2017.
 */
@WebSocket
public class UserRegistry implements Collection<User>{
  private int nextId;
  private List<User> users;
  private HashMap<Integer, User> usersById;
  private HashMap<Session, User> usersBySession;

  public User getUserById(int id) {
    return usersById.get(id);
  }

  public UserRegistry() {
    nextId = 0;
    usersById = new HashMap<>();
    usersBySession = new HashMap<>();
    users = new LinkedList<>();
  }

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    //TODO, do I need to do anything here
    System.out.println("New User");
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    if (usersBySession.containsKey(user)) {
      User u = usersBySession.get(user);
      System.out.println("User " + u.getName() + " has left.");
    } else {
      System.out.println("Unidentified user has left");
    }
  }

  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    System.out.println(message);
    if (message.startsWith("register:")) {
      String register = message.substring(9);
      if (register.equals("newid")) {
        User u = new User(user, nextId);
        users.add(u);
        // Make users accessible through the HashMaps.
        usersById.put(nextId++, u);
        usersBySession.put(user, u);
        try {
          u.getRemote().sendString("userid:" + u.getId());
        } catch (IOException e) { e.printStackTrace(); }
      } else {
        User u = new User(user, nextId);
        users.add(u);
        // Make users accessible through the HashMaps.
        usersById.put(nextId++, u);
        usersBySession.put(user, u);
        try {
          u.getRemote().sendString("userid:" + u.getId());
        } catch (IOException e) { e.printStackTrace(); }
      }
    }
  }









  /*
          IMPLEMENTATION FOR COLLECTION
   */

  @Override
  public int size() {
    return users.size();
  }

  @Override
  public boolean isEmpty() {
    return users.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return users.contains(o);
  }

  @Override
  public Iterator<User> iterator() {
    return users.iterator();
  }

  @Override
  public Object[] toArray() {
    return users.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return users.toArray(a);
  }

  @Override
  public boolean add(User user) {
    return users.add(user);
  }

  @Override
  public boolean remove(Object o) {
    return users.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return users.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends User> c) {
    return users.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return users.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return users.retainAll(c);
  }

  @Override
  public void clear() {
    users.clear();
  }
}