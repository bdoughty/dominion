package edu.brown.cs.dominion.gameutil;

@SuppressWarnings("serial")
public class EmptyDeckException extends Exception {

  public EmptyDeckException() {
    super();
  }

  public EmptyDeckException(String message) {
    super(message);
  }

}
