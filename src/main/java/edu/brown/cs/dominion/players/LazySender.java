package edu.brown.cs.dominion.players;

import edu.brown.cs.dominion.io.send.ButtonCallback;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by henry on 5/7/2017.
 */
class LazySender{
  private static int id = 0;
  List<Integer> toSend = new LinkedList<>();

  public synchronized void sendLazyHand(UserPlayer p, ButtonCallback bc)
    throws UserInteruptedException{
    if (!p.hasUserActions() && toSend.isEmpty()) {
      bc.pressed();
      return;
    }
    int i = id++;
    toSend.add(0);
    try {
      do {
        wait();
      } while (p.hasUserActions() || toSend.get(0) != i);
      toSend.remove(0);
      bc.pressed();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
