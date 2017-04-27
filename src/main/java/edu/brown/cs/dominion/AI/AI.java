package edu.brown.cs.dominion.AI;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.Callback;

/**
 * Created by henry on 4/27/2017.
 */
public interface AI {
  public void play(Game g);
  public void doCallback(Game g, Callback c);
}
