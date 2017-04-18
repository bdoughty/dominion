package edu.brown.cs.dominion.games;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.io.GameEventListener;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub implements GameEventListener {
  private List<User> allUsers;
  private Queue<User> usersTurns;
  private User current;
  private Map<User, Player> userPlayers;
  private Board board;

  public Game(List<User> usersTurns, List<Integer> actionCardIds) {
    usersTurns.forEach(u -> userPlayers.put(u, new Player()));
    this.allUsers = new LinkedList<>(usersTurns);
    this.usersTurns = new LinkedList<>(usersTurns);
    this.current = this.usersTurns.poll();
    this.board = new Board(actionCardIds);
  }

  public User getCurrent() {
    return current;
  }

  public List<User> getAllUsers() {
    return allUsers;
  }

  public Board getBoard() {
    return board;
  }

  private void playerUpdateMap(ClientUpdateMap cm, Player p) {
    cm.actionCount(p.getActions());
    cm.buyCount(p.getBuys());
    cm.goldCount(p.getMoney());
    cm.deckRemaining(p.getDeck().size());
    cm.discardPileSize(p.getDiscard().size());
    cm.hand(p.getHand());
  }

  @Override
  public ClientUpdateMap endBuyPhase(User u, List<Integer> toBuy) {
    assert (current.equals(u));
    Player p = userPlayers.get(u);
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
    userPlayers.get(current).newTurn();

    ClientUpdateMap cm = new ClientUpdateMap(this);
    playerUpdateMap(cm, p);
    cm.turn(current.getId());

    if (board.gameHasEnded()) {
      int highScore = userPlayers
          .get(Collections.max(allUsers, (User one, User two) -> {
            return Integer.compare(userPlayers.get(one).scoreDeck(),
                userPlayers.get(two).scoreDeck());
          })).scoreDeck();
      List<User> winners = allUsers.stream().filter(user -> {
        return userPlayers.get(user).scoreDeck() == highScore;
      }).collect(Collectors.toList());

      cm.winner(winners);
    }

    return cm;
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
  public ClientUpdateMap fullUpdate(User u) {
    ClientUpdateMap cm = new ClientUpdateMap(this);
    playerUpdateMap(cm, userPlayers.get(u));
    // cm.actionCount(userPlayers.get(u).getActions());
    // cm.buyCount(userPlayers.get(u).getBuys());
    // cm.deckRemaining(userPlayers.get(u).getDeck().size());
    // cm.goldCount(userPlayers.get(u).getMoney());
    // cm.hand(userPlayers.get(u).getHand());
    return cm;
  }

}
