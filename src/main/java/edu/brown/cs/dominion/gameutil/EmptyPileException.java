package edu.brown.cs.dominion.gameutil;

@SuppressWarnings("serial")
public class EmptyPileException extends Exception {

  public EmptyPileException() {
    super();
  }

  public EmptyPileException(String message) {
    super(message);
  }

}
