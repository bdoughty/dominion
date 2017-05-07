package edu.brown.cs.dominion.io;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.MessageType;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static edu.brown.cs.dominion.io.send.MessageType.*;

/**
 * Created by henry on 4/14/2017.
 */
@WebSocket
public class Websocket {
  private UserRegistry users;
  private Map<Session, User> usersBySession;
  private Multimap<User, Session> userSessions;
  private Map<String, MessageListener> commands;
  private Map<String, UserMessageListener> allUserCommands;
  private Map<User, Map<String, UserMessageListener>> userCommands;
  private SocketServer serve;

  public Websocket(UserRegistry users, SocketServer serve) {
    this.users = users;
    this.serve = serve;
    usersBySession = new HashMap<>();
    userCommands = new HashMap<>();
    userSessions = HashMultimap.create();
    allUserCommands = new HashMap<>();
    commands = new HashMap<>();
    commands.put("newid", this::registerNewUser);
    commands.put("oldid", this::tryToRegisterOldUser);
    serve.registerGlobalCommands(this);
  }

  public void putCommand(MessageType type, UserMessageListener ml) {
    allUserCommands.put(type.toString(), ml);
  }

  public void send(User u, MessageType type, Object message) {
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

  public void sendAll(MessageType type, Object message) {
    String mess = message.toString();
    for (User u : userSessions.keySet()) {
      send(u, type, message);
    }
  }

  public void sendRaw(User u, String message) {
    for (Session s : userSessions.get(u)) {
      try {
        s.getRemote().sendString(message);
      } catch (IOException e) {
        System.out.println("Failed to send \"" + message + "\" to " +
          "one or more of the client sessions");
        e.printStackTrace();
      }
    }
  }

  public void sendAllRaw(String message) {
    for (User u : userSessions.keySet()) {
      sendRaw(u, message);
    }
  }

  public void send(Session s, MessageType type, Object message) {
    try {
      s.getRemote().sendString(type + ":" + message.toString());
    } catch (IOException e) {
      System.out.println("Failed to send \"" + message.toString() + "\" to " +
        "one or more of the client sessions");
      e.printStackTrace();
    }
  }

  private void tryToRegisterOldUser(Websocket ws, Session ses, String mes) {
    int id = Integer.parseInt(mes);
    if (users.hasUser(id)) {
      User u = users.getById(id);
      usersBySession.put(ses, u);
      userSessions.put(u, ses);
      serve.newSession(ws, u, ses);
    } else {
      registerNewUser(ws, ses, mes);
    }
  }

  private void registerNewUser(Websocket ws, Session ses, String mes) {
    User u = users.registerNewUser();
    usersBySession.put(ses, u);
    userSessions.put(u, ses);
    userCommands.put(u, new HashMap<>());
    send(ses, NEWID, u.getId());
    serve.newUser(ws, u);
    serve.newSession(ws, u, ses);
  }

  private void closeSession(Session s){
    User u = usersBySession.remove(s);
    userSessions.remove(u, s);
  }

  public void registerUserCommand(User u, MessageType command, UserMessageListener
                                  uml){
    userCommands.get(u).put(command.toString().toLowerCase(), uml);
  }

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    // Do nothing
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    closeSession(user);
  }

  @OnWebSocketMessage
  public void onMessage(Session sess, String message) {
    String type = message.substring(0, message.indexOf(':')).toLowerCase();
    String data = message.substring(message.indexOf(':') + 1);
    if (commands.containsKey(type)) {
      commands.get(type).handleMessage(this, sess, data);
    } else if (usersBySession.containsKey(sess)) {
      User u = usersBySession.get(sess);
      if (allUserCommands.containsKey(type)){
        UserMessageListener uml = allUserCommands.get(type);
        uml.handleMessage(this, u, data);
      } else if(userCommands.get(u).containsKey(type)){
        UserMessageListener uml = userCommands.get(u).get(type);
        uml.handleMessage(this, u, data);
      } else {
        System.out.println("unrecognized command for this user");
      }
    }
  }
}
