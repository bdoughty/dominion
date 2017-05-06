package edu.brown.cs.dominion.games;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub {
  private List<Player> allPlayers;
  private Player currentPlayer;
  private Board board;
  private boolean actionPhase = true;

  public Game(List<Player> usersTurns, List<Integer> actionCardIds) {
    this.allPlayers = new LinkedList<>(usersTurns);
    this.board = new Board(actionCardIds);
    currentPlayer = allPlayers.get(0);
  }

  public void play() {
    while(true) {
      for (Player allPlayer : allPlayers) {
        playTurn(allPlayer);
        if (board.gameHasEnded()) { break; }
      }
      if(board.gameHasEnded()) { break; }
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
    List<Integer> toBuy = p.buyCards();
    for (int buyId : toBuy) {
      try {
        Card c = buyCard(buyId, money);
        p.buyCard(c);
        money -= c.getCost();
        //sendServerMessage(u.getName() + " bought " + c.toString() + ".");
      } catch (TooExpensiveException | EmptyPileException | NoPileException tee) {
        System.out.println(tee.getMessage());
      }
    }
  }

  public Card buyCard(int buyId, int money) throws NoPileException, TooExpensiveException, EmptyPileException {
    return board.buyCard(buyId, money);
  }

  public void win(){
    System.out.println("Game Over");
    // nothing is necessary
  }

  public void playTurn(Player p) {
    currentPlayer = p;
    p.newTurn();
    doActions(p);
    buyPhase(p);
    p.endTurn();
  }

  public int getCurrentPlayerId(){
    return currentPlayer.getId();
  }

  public void doActions(Player p) {
    int loc;
    System.out.println("new action loop");
    while(-1 != (loc = p.playHandAction())){
      System.out.println("played card " + loc);
      try {
        Card c = p.play(loc);
        c.play(p);
        // sendServerMessage(u.getName() + " played " + c.toString() + ".");
      } catch (NoActionsException ignored) {}
      catch (NotActionException e) {}
      System.out.println("trying new action");
    }
    p.endActionPhase();
  }

  public void othersDraw(Player curr ,int numCards) {
    for (Player p : allPlayers) {
      if (p != curr) {
        p.draw(numCards);
      }
    }
  }

  public void trash(Card c) { board.trashCard(c); }

  public Card gain(int id) throws EmptyPileException, NoPileException {
    return board.gainCard(id);
  }

  public void othersGainCurse(Player curr) {
    for (Player p :allPlayers) {
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
    //TODO end turn and whatnot
    allPlayers.remove(p);
  }
}
