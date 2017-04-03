package edu.brown.cs.dominion.user;

/**
 * Created by henry on 4/2/2017.
 */
public enum MessageType {
  NEWID ("userid");

  private final String name;
  MessageType(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
