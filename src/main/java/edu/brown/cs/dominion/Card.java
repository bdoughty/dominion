package edu.brown.cs.dominion;

/**
 * Created by henry on 3/22/2017.
 */
public abstract class Card{
  private int id;
  private int cost;
  //Note all other variables should be transient to avoid lengthy GSON
  // calls.

  public int getId() {
    return getId();
  }
  public static Card getCardFromId(int id) {
    //TODO this
    return null;
  }

  public static Card getCardFromName(String name) {
    //TODO this
    return null;
  }
}
