package edu.brown.cs.dominion.io;

/**
 * Exception when trying to access a user that does not exist.
 * Created by henry on 5/7/2017.
 */
public class NoUserException extends Exception{
  public NoUserException(String msg){
    super(msg);
  }
}
