package edu.brown.cs.dominion.user;

/**
 * Interface To Allow The Sending of Data Across the Websocket.
 * Created by henry on 4/2/2017.
 */
public interface Sender {
  boolean send(String s);
  default boolean send(MessageType type, String s) {
    //String concatenation is slow, but this does not matter given the
    // frequency of calls.
    return send(type.toString() + ":" + s);
  }
  default boolean send(MessageType type, Object o) {
    return send(type.toString() + ":" + o.toString());
  }
}