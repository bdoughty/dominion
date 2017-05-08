package edu.brown.cs.dominion.io;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import edu.brown.cs.dominion.io.send.MessageType;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * A class that allows easy communication with users (on multiple tabs rather
 * than individual websocket sessions) It also is integrated with a
 * SocketServer which registers new commands for users and generally
 * organizes communications.
 *
 * Created by henry on 4/14/2017.
 */
@WebSocket
public class Websocket {
  private UserRegistry users;
  private Map<Session, User> usersBySession;
  private Multimap<User, Session> userSessions;
  private Map<String, MessageListener> commands;
  private Map<String, UserMessageListener> allUserCommands;
  private SocketServer serve;

  /**
   * Setup websocket with maps associating the users and synchronous
   * associations between the users and their sessions in both directions
   * @param users the user registry the server is using
   * @param serve The socket server that will provide the majority of the
   *              websocket functionality.
   */
  public Websocket(UserRegistry users, SocketServer serve) {
    this.users = users;
    this.serve = serve;
    usersBySession = Collections.synchronizedMap(new HashMap<>());
    userSessions = Multimaps.synchronizedMultimap(HashMultimap.create());
    allUserCommands = new HashMap<>();
    commands = new HashMap<>();
    commands.put("newid", this::registerNewUser);
    commands.put("oldid", this::tryToRegisterOldUser);
    serve.registerGlobalCommands(this);
  }

  /**
   * Add a new command to listen for on all users.
   * @param type enum represting the command and containing its string
   *             representation.
   * @param ml the listener to be called (websocket, user, message) upon the
   *           recieval of this command
   */
  public void putCommand(MessageType type, UserMessageListener ml) {
    allUserCommands.put(type.toString(), ml);
  }

  /**
   * Send a message to all of the sessions associated with a user
   * @param u the user to send the message to.
   * @param type the type of the message to send
   * @param message the object which contains the string data to be sent to
   *                the client.
   */
  public synchronized void send(User u, MessageType type, Object message) {
    for (Session s : userSessions.get(u)) {
      try {
        s.getRemote().sendString(type + ":" + message.toString());
      } catch (IOException e) {
        System.out.println("Failed to send \"" + message.toString() + "\" to " +
          "one or more of the client sessions");
        e.printStackTrace();
      }
    }
  }

  /**
   * Send a message to all of the sessions registered on the websocket.
   * @param type the type of message to be sent.
   * @param message the object which contains the string data to be sent to
   *                the client.
   */
  public synchronized void sendAll(MessageType type, Object message) {
    String mess = message.toString();
    for (User u : userSessions.keySet()) {
      send(u, type, message);
    }
  }

  /**
   * Send a message to a single session
   * @param s the session to send the message to
   * @param type the message type of the message to be sent
   * @param message the message itself in object form (toString is used to
   *                extract the message).
   */
  public synchronized void send(Session s, MessageType type, Object message) {
    try {
      s.getRemote().sendString(type + ":" + message.toString());
    } catch (IOException e) {
      System.out.println("Failed to send \"" + message.toString() + "\" to " +
        "one or more of the client sessions");
      e.printStackTrace();
    }
  }

  /**
   * If a user claims to already have an id stored in its cookies this method
   * will detect if such a user exists.
   *
   * If such a user does exist it will associate the new session with the old
   * user, otherwise it will create a new user for the session.
   *
   * @param ws the websocket itself
   * @param ses the session sending the message
   * @param mes the message the websocket sent
   */
  private void tryToRegisterOldUser(Websocket ws, Session ses, String mes) {
    int id = Integer.parseInt(mes);
    if (users.hasUser(id)) {
      try {
        User u = users.getById(id);
        usersBySession.put(ses, u);
        userSessions.put(u, ses);
        serve.newSession(ws, u, ses);

      } catch (NoUserException e) {
        registerNewUser(ws, ses, mes);
      }
    } else {
      registerNewUser(ws, ses, mes);
    }
  }

  /**
   * Register a session that does not have a user to a new user.
   * @param ws the websocket itself.
   * @param ses the session to associate.
   * @param mes the message the user sent (not users, empty).
   */
  private void registerNewUser(Websocket ws, Session ses, String mes) {
    User u = users.registerNewUser();
    usersBySession.put(ses, u);
    userSessions.put(u, ses);
    send(ses, NEWID, u.getId());
    serve.newUser(ws, u);
    serve.newSession(ws, u, ses);
  }

  /**
   * remove a session from the server, but does not delete the user if there
   * are no sessions associated with it because for example a refresh would
   * trigger such a condition.
   * @param s the session to be disassociated.
   */
  private void closeSession(Session s){
    if (usersBySession.containsKey(s)) {
      User u = usersBySession.remove(s);
      userSessions.remove(u, s);
    }
  }

  /**
   * Register the connection of a new user. Does not do anything, because the
   * first communication with the client must be client initiated (user
   * authentication).
   * @param user the session that was registered
   */
  @OnWebSocketConnect
  public void onConnect(Session user) {
    // Do nothing
  }

  /**
   * Close a session when the session disconnects (disassociates it with its
   * user)
   * @param user the session that is leaving
   * @param statusCode the status of that session
   * @param reason the reason why the session left
   */
  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    closeSession(user);
  }

  /**
   * upon receiving a command from a user, run the corresponding command as
   * per the publish subscribe paradigm.
   * @param sess the session that sent a message.
   * @param message the message itself in the format "(.*):(.*)"
   *                where the first group is the type of the message and the
   *                second group is any data associated with a message of
   *                that type.
   */
  @OnWebSocketMessage
  public void onMessage(Session sess, String message) {
    if (message.contains(":")) {
      String type = message.substring(0, message.indexOf(':')).toLowerCase();
      String data = message.substring(message.indexOf(':') + 1);
      if (commands.containsKey(type)) {
        try {
          commands.get(type).handleMessage(this, sess, data);
        } catch (Exception e) {
          System.out.println("ERROR: handling the message \"" + message + "\"");
          System.out.println("ERROR MESSAGE: " + e.getMessage());
        }
        return;
      } else if (usersBySession.containsKey(sess)) {
        User u = usersBySession.get(sess);
        if (allUserCommands.containsKey(type)) {
          UserMessageListener uml = allUserCommands.get(type);
          try {
            uml.handleMessage(this, u, data);
          } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: handling the message \"" + message + "\"");
            System.out.println("ERROR MESSAGE: " + e.getMessage());
          }
          return;
        }
      }
    }
    System.out.println("ERROR: received unrecognized command");
  }
}
