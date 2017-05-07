package edu.brown.cs.dominion.io;

import java.awt.Color;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

/**
 * A user which is associated with many different sessions across websockets.
 *
 * Registered and constructed in the UserRegistry
 *
 * Created by henry on 3/22/2017.
 */
public class User {
  // list of default names
  private static ImmutableList<String> names =
      ImmutableList.of("Mrs. McCave", "Dave",
          "Bodkin Van Horn", "Hoos-Foos", "Snimm", "Hot-Shot", "Sunny Jim",
          "Shadrack", "Blincky", "Stuffy", "Stinky", "Putt-Putt", "Moon Face",
          "Marvin O'Gravel Ballon Face", "Ziggy", "Soggy Muff", "Buffalo Bill",
          "Biffalo Buff", "Sneepy", "Weepy Weed", "Paris Garters",
          "Harris Tweed", "Sir Michael Carmichael Zutt", "Oliver Boliver Butt",
          "Zanzibar Buck-Buck McFate");

  private int id;
  private String color;
  private String name;

  /**
   * Constructor the a new User
   * @param id the id the user should have
   * @param playerNumber the number which designates what the name of the
   *                     user should be by default.
   */
  public User(int id, int playerNumber) {
    color = makeColor();
    this.id = id;
    this.name = makeName(playerNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof User) {
      User that = (User) o;
      return (this.id == that.id);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }


  /**
   * Getter for the name of the user, will return a string conversion of the
   * users ID.
   *
   * @return the name of the user.
   */
  public String getName() {
    return name;
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

  /**
   * make a random color with 0.5 saturation and 1.0 brightness
   * @return the color in javascript format.
   */
  private String makeColor() {
    Color c = Color.getHSBColor((float) Math.random(), 0.5f, 1f);
    return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
  }

  private String makeName(int id) {
    if (id <= 24) {
      return names.get(id);
    } else {
      return "Dave " + (id - 23);
    }
  }

  /**
   * Set the users name to a new name.
   * @param name the new name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }
}
