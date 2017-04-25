package edu.brown.cs.dominion.io.send;

import java.util.List;

public class Callback{
  private List<Integer> handIds;
  private List<Integer> boardIds;
  private SelectCallback callback;

  public Callback(List<Integer> boardIds, List<Integer> handIds,
    SelectCallback callback){
    this.boardIds = boardIds;
    this.callback = callback;
    this.handIds = handIds;
  }

  public List<Integer> getHandIds() {
    return handIds;
  }

  public List<Integer> getBoardIds() {
    return boardIds;
  }

  public SelectCallback getCallback() {
    return callback;
  }
}
