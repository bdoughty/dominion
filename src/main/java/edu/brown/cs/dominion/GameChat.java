package edu.brown.cs.dominion;

import edu.brown.cs.dominion.io.Websocket;

import java.util.Collection;

/**
 * Created by henry on 4/24/2017.
 */
public class GameChat extends Chat{
  private Websocket ws;
  private Collection<User> users;

  public GameChat(Websocket ws, Collection<User> users) {
    this.ws = ws;
    this.users = users;
  }

  public void send(User u, String s){
    String message = getMessage(u.getName(), u.getColor(), s);
  }

  public void serverSend(String s){
    String message = getMessage("Server", "#000", s);
  }
}
