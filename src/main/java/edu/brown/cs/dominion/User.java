package edu.brown.cs.dominion;

import java.awt.Color;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

/**
 * A wrapper for session that allows users to contain extra information
 * associated with the user concept.
 *
 * Created by henry on 3/22/2017.
 */
public class User {
  private static ImmutableList<String> names =
      ImmutableList.copyOf(Arrays.asList("Mrs. McCave", "Dave",
          "Bodkin Van Horn", "Hoos-Foos", "Snimm", "Hot-Shot", "Sunny Jim",
          "Shadrack", "Blincky", "Stuffy", "Stinky", "Putt-Putt", "Moon Face",
          "Marvin O'Gravel Ballon Face", "Ziggy", "Soggy Muff", "Buffalo Bill",
          "Biffalo Buff", "Sneepy", "Weepy Weed", "Paris Garters",
          "Harris Tweed", "Sir Michael Carmichael Zutt", "Oliver Boliver Butt",
          "Zanzibar Buck-Buck McFate"));

  private int id;
  private String color;
  private String name;
  // private transient Player player;

  public User(int id, int startId) {
    color = makeColor();
    this.id = id;
    this.name = makeName(id - startId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof User) {
      User that = (User) o;
      return (this.id == that.id);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  // public boolean hasPlayer() {
  // return player != null;
  // }
  //
  // public Player getPlayer() {
  // return player;
  // }

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

  // public void initPlayer() {
  // player = new Player();
  // }

  private String makeName(int id) {
    if (id <= 24) {
      return names.get(id);
    } else {
      return "Dave " + (id - 23);
    }
  }
}
