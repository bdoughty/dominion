package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 4/2/2017.
 */
public enum MessageType {
  NEWID ("userid"),
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
  INIT_GAME ("init"),
  NEW_GAME ("create"),
  CANCEL_SELECT("cancel"),
  BUTTON_RESPONSE ("button"),
  EXIT_GAME("exit"),
  NOTIFY ("notify"),
  ACTIONS("actions"),
  BUYS("buys"),
  GOLD("gold"),
  PLAYER_ACTIONS ("playeractions"),
  HAND ("hand"),
  DECK_SIZE ("decksize"),
  DISCARD_SIZE ("discardsize"),
  BOARD ("board"),
  PHASE ("phase"),
  TURN("turn"),
  WINNER("winner");


  private final String name;
  MessageType(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
