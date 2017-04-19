package edu.brown.cs.dominion.gameutil;

import java.util.Queue;

public class DeprecatedGameHandler {

  private Player current;
  private Queue<Player> others;
  private Board board;

  public DeprecatedGameHandler(Queue<Player> players, Board board) {
    this.others = players;
    this.board = board;
    this.current = this.others.poll();
  }

  public void incrementActions() {
    this.current.incrementActions();
  }

  public void incrementBuys() {
    this.current.incrementBuys();
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    this.current.incrementAdditionalMoney(adnlMoney);
  }

  public void currentDraw(int numCards) {
    this.current.draw(numCards);
  }

  public void militia() {
    for (Player p : others) {
      if (!p.hasMoat()) {

      }
    }

  }

}
