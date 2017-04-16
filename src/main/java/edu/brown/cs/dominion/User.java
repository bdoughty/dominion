package edu.brown.cs.dominion;

import java.awt.Color;

import edu.brown.cs.dominion.gameutil.Player;

/**
 * A wrapper for session that allows users to contain extra information
 * associated with the user concept.
 *
 * Created by henry on 3/22/2017.
 */
public class User {
  private int id;
  private String color;
  private String name;
  private transient Player player;

  public User(int id) {
    color = makeColor();
    this.id = id;
    this.name = id == 0 ? "" : "Dave " + id;
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

  private String makeName(int id){
    switch (id) {
      case 0: return "Mrs. McCave";
      case 1: return "Dave";
      case 2: return "Bodkin Van Horn";
      case 3: return "Hoos-Foos";
      case 4: return "Snimm";
      case 5: return "Hot-Shot";
      case 6: return "Sunny Jim";
      case 7: return "Shadrack";
      case 8: return "Blincky";
      case 9: return "Stuffy";
      case 10: return "Stinky";
      case 11: return "Putt-Putt";
      case 12: return "Moon Face";
      case 13: return "Marvin O'Gravel Ballon Face";
      case 14: return "Ziggy";
      case 15: return "Soggy Muff";
      case 16: return "Buffalo Bill";
      case 17: return "Biffalo Buff";
      case 18: return "Sneepy";
      case 19: return "Weepy Weed";
      case 20: return "Paris Garters";
      case 21: return "Harris Tweed";
      case 22: return "Sir Michael Carmichael Zutt";
      case 23: return "Oliver Boliver Butt";
      case 24: return "Zanzibar Buck-Buck McFate";
      default: return "Dave " + (id - 23);
    }
  }
}
