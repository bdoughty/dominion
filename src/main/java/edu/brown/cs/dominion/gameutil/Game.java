package edu.brown.cs.dominion.gameutil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.brown.cs.dominion.action.ActionCenter;

public class Game {

  private Queue<Player> players;
  private ActionCenter ac;
  private Board board;

  public Game(List<Integer> playerIds, List<Integer> cardIds) {
    this.players = new LinkedList<>();

    for (int pid : playerIds) {
      this.players.add(new Player(pid));
    }

    this.ac = new ActionCenter();
    this.board = new Board(cardIds);
  }

  public void playGame() {
    while (!this.board.gameHasEnded()) {

    }

    Player winner = Collections.max(players,
        (p1, p2) -> Integer.compare(p1.scoreDeck(), p2.scoreDeck()));
  }

}
