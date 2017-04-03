package edu.brown.cs.dominion.user;

import org.eclipse.jetty.websocket.api.Session;
import java.awt.Color;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A wrapper for session that allows users to contain extra information
 * associated with the user concept.
 *
 * Created by henry on 3/22/2017.
 */
public class User implements Sender{
  private int id;
  private Color c;
  private String name;
  private Set<Session> sessions;

  public User(int id) {
    sessions = new HashSet<>();
    c = makeColor();
    this.id = id;
  }

  public void addSession(Session s) {
    sessions.add(s);
  }

  public void cleanSessions() {
    sessions.removeIf(s -> !s.isOpen());
  }

  public boolean removeSession(Session s) {
    if(sessions.contains(s)) {
      sessions.remove(s);
      return true;
    }
    return false;
  }

  /**
   * Getter for the name of the user, will return a string conversion of the
   * users ID.
   * @return the name of the user.
   */
  public String getName() {
    if (name != null) {
      return name;
    } else {
      return Integer.toString(id);
    }
  }

  public int getId() {
    return id;
  }

  public String getColor() {
    return "rgb("+ c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
  }

  private Color makeColor(){
    return Color.getHSBColor((float) Math.random(), 0.5f, 0.5f);
  }

  @Override
  public boolean send(String message) {
    boolean sent = false;
    for (Session s : sessions) {
      try {
        s.getRemote().sendString(message);
        sent = true;
      } catch (IOException e) {
        System.out.println("ERROR: sending message " + message + " to the " +
            "user #" + id);
      }
    }
    return sent;
  }

  public boolean hasOpenSessions() {
    for (Session s : sessions) {
      if (s.isOpen()) {
        return true;
      }
    }
    return false;
  }
}
