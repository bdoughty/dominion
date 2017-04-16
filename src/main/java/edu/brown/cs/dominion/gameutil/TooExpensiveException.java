package edu.brown.cs.dominion.gameutil;

@SuppressWarnings("serial")
public class TooExpensiveException extends RuntimeException {

  public TooExpensiveException() {
    super();
  }

  public TooExpensiveException(String message) {
    super(message);
  }

}
