package edu.brown.cs.dominion.games;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.Player;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub {
  private List<Player> allPlayers;
  private Player currentPlayer;
  private Board board;
  private boolean actionPhase = true;
  private long turnStartTime;

  private boolean turnCanceled = false;

  public Game(List<Player> usersTurns, List<Integer> actionCardIds) {
    this.allPlayers = new LinkedList<>(usersTurns);
    this.board = new Board(actionCardIds);
    currentPlayer = allPlayers.get(0);
  }

  public void play() {
    while (true) {
      for (Player allPlayer : allPlayers) {
        playTurn(allPlayer);
        if (board.gameHasEnded()) {
          break;
        }
      }
      if (board.gameHasEnded()) {
        break;
      }
    }
    win();
  }

  public List<Player> getPlayers() {
    return allPlayers;
  }

  public Board getBoard() {
    return board;
  }

  public void buyPhase(Player p) {
    actionPhase = true;
    int money = p.getMoney();
    List<Integer> toBuy = null;
    try {
      toBuy = p.buyCards();
    } catch (UserInteruptedException e) {
      return;
    }
    for (int buyId : toBuy) {
      try {
        Card c = buyCard(buyId, money);
        p.buyCard(c);
        money -= c.getCost();
        // sendServerMessage(u.getName() + " bought " + c.toString() + ".");
      } catch (TooExpensiveException | EmptyPileException
          | NoPileException tee) {
        System.out.println(tee.getMessage());
      }
    }
  }

  public Card buyCard(int buyId, int money)
      throws NoPileException, TooExpensiveException, EmptyPileException {
    return board.buyCard(buyId, money);
  }

  public void win() {
    System.out.println("Game Over");
    // nothing is necessary
  }

  public void playTurn(Player p) {
    Thread turnEnder = new Thread(() -> {
      try {
        turnStartTime = System.currentTimeMillis();
        Thread.sleep(61000);
        cancelTurn();
        System.out.println("ending turn");
      } catch (InterruptedException ignored) { }
    });
    turnEnder.start();
    currentPlayer = p;
    p.newTurn();
    if(!turnCanceled) {doActions(p);}
    if(!turnCanceled) {buyPhase(p);}
    p.endTurn();
    turnCanceled = false;
    turnEnder.interrupt();
  }

  public void cancelTurn(){
    turnCanceled = true;
    synchronized (currentPlayer) {
      currentPlayer.notifyAll();
    }
  }

  public int getCurrentPlayerId() {
    return currentPlayer.getId();
  }

  public void doActions(Player p) {
    int loc;
    System.out.println("new action loop");
    try {
      while(-1 != (loc = p.playHandAction()) && !turnCanceled){
        System.out.println("played card " + loc);
        try {
          Card c = p.play(loc);
          c.play(p);
          // sendServerMessage(u.getName() + " played " + c.toString() + ".");
        } catch (NoActionsException | NotActionException ignored) {}
        System.out.println("trying new action");
      }
    } catch (UserInteruptedException ignored) { }
    p.endActionPhase();
  }

  public void othersDraw(Player curr, int numCards) {
    for (Player p : allPlayers) {
      if (p != curr) {
        p.draw(numCards);
      }
    }
  }

  public void trash(Card c) {
    board.trashCard(c);
  }

  public Card gain(int id) throws EmptyPileException, NoPileException {
    return board.gainCard(id);
  }

  public void othersGainCurse(Player curr) {
    for (Player p : allPlayers) {
      if (p != curr) {
        try {
          p.gain(gain(6), false, false);
        } catch (EmptyPileException | NoPileException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public Map<Integer, Integer> getVictoryPointMap() {
    Map<Integer, Integer> vps = new HashMap<>();
    allPlayers.forEach(p -> vps.put(p.getId(), p.scoreDeck()));
    return vps;
  }

  public void removeUser(Player p) {
    // TODO end turn and whatnot
    if(p == currentPlayer) {
      cancelTurn();
    }
    allPlayers.remove(p);
  }

  public int getTimeLeftOnTurn(){
    return (int) (System.currentTimeMillis() - turnStartTime);
  }
}
