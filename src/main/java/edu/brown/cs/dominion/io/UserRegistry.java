package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * A way that hooks into the WebSocket system in order to keep track of all
 * the different users.
 * Created by henry on 3/28/2017.
 */
@WebSocket
public class UserRegistry implements Collection<User>{
  private int nextId;
  private Map<Integer, User> usersById;
  private Map<Session, User> usersBySession;
  private Map<String, MessageConsumer> commands;

  public User getUserById(int id) {
    return usersById.get(id);
  }

  public UserRegistry() {
    nextId = 0;
    usersById = new HashMap<>();
    usersBySession = new HashMap<>();
    commands = new HashMap<>();

    commands.put("newid", this::registerNewUser);
    commands.put("oldid", this::tryToRegisterOldUser);
  }

  private void tryToRegisterOldUser(Session ses, String mes) {
    int id = Integer.parseInt(mes);
    if (usersById.containsKey(id)) {
      User u = usersById.get(id);
      u.addSession(ses);
      usersBySession.put(ses, u);
    } else {
      registerNewUser(ses, mes);
    }
  }

  private void registerNewUser(Session ses, String mes) {
    User u = new User(nextId);
    u.addSession(ses);
    usersBySession.put(ses, u);
    usersById.put(nextId, u);
    u.send(NEWID, nextId);
    nextId++;
  }

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    //Not an important event because we must weight for the user to tell us
    // whether or not they already have an id stored in cookies.
    System.out.println("New Session");
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    if (usersBySession.containsKey(user)) {
      User u = usersBySession.get(user);
      u.removeSession(user);
      System.out.println("Session removed from user");
    } else {
      System.out.println("Mystery session removed.");
    }
  }

  @OnWebSocketMessage
  public void onMessage(Session sess, String message) {
    try {
      String commandType = message.substring(0, message.indexOf(':'));
      commandType = commandType.toLowerCase(Locale.getDefault());
      MessageConsumer command = commands.get(commandType);

      if (command == null) {
        throw new RuntimeException("No command mapped for \"" + commandType +
            "\"");
      }

      command.accept(sess, message.substring(message.indexOf(':') + 1));
    } catch (IndexOutOfBoundsException e) {
      throw new RuntimeException("No semicolon contained in the message " +
          message);
    }
  }









  /*
          IMPLEMENTATION FOR COLLECTION
   */

  @Override
  public int size() {
    return usersById.size();
  }

  @Override
  public boolean isEmpty() {
    return usersById.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return usersById.containsValue(o);
  }

  @Override
  public Iterator<User> iterator() {
    return usersById.values().iterator();
  }

  @Override
  public Object[] toArray() {
    return usersById.values().toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return usersById.values().toArray(a);
  }

  @Deprecated
  @Override
  public boolean add(User user) {
    //CANNOT ADD USER FROM THE OUTSIDE
    return false;
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof User) {
      usersById.remove(((User) o).getId());
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return usersById.values().containsAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(Collection<? extends User> c) {
    //CANNOT ADD USER FROM THE OUTSIDE
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    for (Object o : c) {
      remove(o);
    }
    return true;
  }

  @Deprecated
  @Override
  public boolean retainAll(Collection<?> c) {
    //CANNOT RETAIN USER FROM THE OUTSIDE
    return false;
  }

  @Override
  public void clear() {
    usersById.clear();
    usersBySession.clear();
  }
}