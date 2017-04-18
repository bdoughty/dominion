package edu.brown.cs.dominion.games;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.io.GameEventListener;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.GameInit;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub implements GameEventListener{

  private Queue<User> usersTurns;
  private User current;
  private Board board;

  // I'm still playing around with when to use User and when to use Player, so
  // this will probably get cleaner.
  public Game(List<User> usersTurns, List<Integer> actionCardIds) {
    this.usersTurns = new LinkedList<>(usersTurns);
    this.current = this.usersTurns.poll();
    this.board = new Board(actionCardIds);
  }

  public Queue<User> getUsers(){
    return usersTurns;
  }

  @Override
  public ClientUpdateMap endBuyPhase(User u, List<Integer> toBuy) {
    assert (current.equals(u));
    Player p = u.getPlayer();
    int money = p.getMoney();

    for (int buyId : toBuy) {
      try {
        Card c = board.buyCard(buyId, money);
        p.buyCard(c);
        money -= c.getCost();
      } catch (TooExpensiveException tee) {
      }
    }

    p.endTurn();
    usersTurns.add(u);
    current = usersTurns.poll();
    current.getPlayer().newTurn();

    // I'm not sure which updates need to be passed back for all of these.
    return null;
  }

  public Board getBoard(){
    return board;
  }

  @Override
  public ClientUpdateMap doAction(User u, int LocInHand) {
    // TODO
    return null;
  }

  @Override
  public ClientUpdateMap endActionPhase(User u) {
    // TODO
    return null;
  }

  @Override
  public GameInit sendStartingInfo() {
    // TODO
    return null;
  }

  @Override
  public ClientUpdateMap fullUpdate(User u) {
    // TODO
    return null;
  }

}
