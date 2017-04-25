package edu.brown.cs.dominion.io.send;

import java.util.List;

public class Callback{
  private List<Integer> handIds;
  private List<Integer> boardIds;
  private SelectCallback callback;
  private boolean stoppable;
  private CancelHandler ch;

  public Callback(List<Integer> boardIds, List<Integer> handIds,
    SelectCallback callback){
    this.boardIds = boardIds;
    this.callback = callback;
    this.handIds = handIds;
    stoppable = false;
  }

  public Callback(List<Integer> boardIds, List<Integer> handIds,
                  SelectCallback callback, CancelHandler ch){
    this.boardIds = boardIds;
    this.callback = callback;
    this.handIds = handIds;
    this.stoppable = true;
    this.ch = ch;
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

  public boolean isStoppable() {
    return stoppable;
  }

  public CancelHandler getCancelHandler() {
    return ch;
  }
}
