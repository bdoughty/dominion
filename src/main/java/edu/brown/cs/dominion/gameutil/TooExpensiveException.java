package edu.brown.cs.dominion.gameutil;

@SuppressWarnings("serial")
public class TooExpensiveException extends Exception {

  public TooExpensiveException() {
    super();
  }

  public TooExpensiveException(String message) {
    super(message);
  }

}
