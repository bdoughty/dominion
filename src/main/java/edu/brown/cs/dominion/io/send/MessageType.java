package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 4/2/2017.
 */
public enum MessageType {
  NEWID ("userid"),
  UPDATE_MAP ("updatemap"),
  STOP_HOLD ("stophold"),
  CHAT ("chat");

  private final String name;
  MessageType(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
