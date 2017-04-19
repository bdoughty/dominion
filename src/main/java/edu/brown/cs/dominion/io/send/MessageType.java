package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 4/2/2017.
 */
public enum MessageType {
  NEWID ("userid"),
  UPDATE_MAP ("updatemap"),
  GLOBAL_UPDATE_MAP ("globalmap"),
  STOP_HOLD ("stophold"),
  CHAT ("chat"),
  END_BUY ("endbuy"),
  END_ACTION ("endaction"),
  DO_ACTION ("doaction"),
  SELECTION ("select"),
  PENDING_GAMES ("pending"),
  JOIN_GAME ("join"),
  JOIN_RESPONSE ("joinresponse"),
  LEAVE ("leave"),
  LEAVE_RESPONSE ("leaveresponse"),
  REDIRECT ("redirect"),
  INIT_GAME ("init");


  private final String name;
  MessageType(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
