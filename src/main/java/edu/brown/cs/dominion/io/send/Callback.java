package edu.brown.cs.dominion.io.send;

import java.util.List;

public class Callback{
  List<Integer> handIds;
  List<Integer> boardIds;
  SelectCallback callback;

  public Callback(List<Integer> boardIds, List<Integer> handIds,
    SelectCallback callback){
    this.boardIds = boardIds;
    this.callback = callback;
    this.handIds = handIds;
  }
}
