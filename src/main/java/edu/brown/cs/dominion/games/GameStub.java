package edu.brown.cs.dominion.games;

/**
 * Created by henry on 4/15/2017.
 */
public class GameStub {
  private static int nextId = 0;
  private int id;

  public GameStub() {
    this.id = nextId++;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof GameStub) {
      return ((GameStub) o).getId() == getId();
    }
    return false;
  }
}
