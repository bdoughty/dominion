package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 4/25/2017.
 */
@FunctionalInterface
public interface CancelHandler {
  ClientUpdateMap cancel();
}
