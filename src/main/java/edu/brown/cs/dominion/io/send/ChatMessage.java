package edu.brown.cs.dominion.io.send;

import edu.brown.cs.dominion.User;

/**
 * Wrapper for GSON that is a chat message.
 * Created by henry on 4/2/2017.
 */
public class ChatMessage {
  private String name;
  private String color;
  private String message;

  public ChatMessage(User u, String msg) {
    this.name = u.getName();
    this.message = msg;
    this.color = u.getColor();
  }

  public ChatMessage(String msg) {
    this.name = "SERVER";
    this.message = msg;
    this.color = "#444";
  }
}
