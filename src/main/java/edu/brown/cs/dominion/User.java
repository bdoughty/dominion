package edu.brown.cs.dominion;

import java.awt.Color;

import edu.brown.cs.dominion.gameutil.Player;

/**
 * A wrapper for session that allows users to contain extra information
 * associated with the user concept.
 *
 * Created by henry on 3/22/2017.
 */
public class User{
  private int id;
  private String color;
  private String name;
  private transient Player player;

  public User(int id) {
    color = makeColor();
    this.id = id;
    this.name = id == 0 ? "Mrs. McCave" : "Dave " + id;
  }

  public boolean hasPlayer() {
    return player != null;
  }

  public Player getPlayer() {
    return player;
  }

  /**
   * Getter for the name of the user, will return a string conversion of the
   * users ID.
   *
   * @return the name of the user.
   */
  public String getName() {
    if (name != null) {
      return name;
    } else {
      return Integer.toString(id);
    }
  }

  /**
   * Getter for id.
   *
   * @return user id.
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for the color in html friendly form.
   *
   * @return the color as a string.
   */
  public String getColor() {
    return color;
  }

  private String makeColor() {
    Color c = Color.getHSBColor((float) Math.random(), 0.5f, 1f);
    return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
  }
}
