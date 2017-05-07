package edu.brown.cs.dominion.io.send;

import java.util.List;

public class Callback{
  private List<Integer> handIds;
  private List<Integer> boardIds;
  private boolean stoppable;
  private String name;

  public Callback(List<Integer> boardIds, List<Integer> handIds, String
    name, boolean stoppable){
    this.boardIds = boardIds;
    this.handIds = handIds;
    this.name = name;
    this.stoppable = stoppable;
  }

  public List<Integer> getHandIds() {
    return handIds;
  }

  public List<Integer> getBoardIds() {
    return boardIds;
  }

  public boolean isStoppable() {
    return stoppable;
  }

  public String getName() {
    return name;
  }
}
