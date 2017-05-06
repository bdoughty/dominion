package edu.brown.cs.dominion.players;

import java.util.concurrent.Executor;

/**
 * Created by henry on 5/6/2017.
 */
public class UserInteruptedException extends Exception {
  public UserInteruptedException(){

  }

  public UserInteruptedException(String message){
    super(message);
  }
}
