package edu.brown.cs.dominion.AI;

import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.Callback;

public class AIPlayer extends User implements AI {
  private Strategy st;

  public AIPlayer(int id, int level) {
    super(id);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void play(Game g) {
    // TODO Auto-generated method stub

  }

  @Override
  public void doCallback(Game g, Callback c) {
    // TODO Auto-generated method stub

  }

}
