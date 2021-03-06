package edu.brown.cs.dominion;

import java.util.Collection;

import edu.brown.cs.dominion.io.User;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.MessageType;

/**
 * Created by henry on 4/24/2017.
 */
public class GameChat extends Chat {
  private Websocket ws;
  private Collection<User> users;

  public GameChat(Websocket ws, Collection<User> users) {
    this.ws = ws;
    this.users = users;
  }

  public void send(User u, String s) {
    String message = getMessage(u.getName(), u.getColor(), s);
    sendAll(message);
  }

  public void serverSend(String s){
    String message = getMessage("Server", "#fff", s);
    sendAll(message);
  }

  public void spambotSend(String s) {
    String message = getMessage("Spammy Spammerson", "#706F6F", s);
    sendAll(message);
  }

  private void sendAll(String message) {
    for (User u : users) {
      ws.send(u, MessageType.CHAT, message);
    }
  }

  public void serverSendToUser(String s, User u) {
    String message = getMessage("Server", "#fff", s);
    ws.send(u, MessageType.CHAT, message);
  }

  public void removeUser(User u){
    users.remove(u);
  }
}
