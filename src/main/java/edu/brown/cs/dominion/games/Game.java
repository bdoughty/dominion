package edu.brown.cs.dominion.games;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
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

  boolean actionPhase = true;

  public Game(List<User> usersTurns, List<Integer> actionCardIds) {
    userPlayers = new HashMap<>();
    usersTurns.forEach(u -> userPlayers.put(u, new Player()));
    this.allUsers = new LinkedList<>(usersTurns);
    this.usersTurns = new LinkedList<>(usersTurns);
    this.current = this.usersTurns.poll();
    userPlayers.get(current).newTurn();
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
    if (p.equals(userPlayers.get(current))) {
      cm.setPhase(actionPhase);
    }
  }

  @Override
  public ClientUpdateMap endBuyPhase(User u, List<Integer> toBuy) {
    assert (current.equals(u));
    actionPhase = true;

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
    assert (current.equals(u));
    Player p = userPlayers.get(u);
    // TODO error check this to make sure it's an action, valid pos, etc.
    Card c = p.getHand().get(LocInHand);
    p.discard(new LinkedList<>(ImmutableList.of(LocInHand)));
    c.play(this);
    p.decrementActions();

    ClientUpdateMap cm = new ClientUpdateMap(this);
    playerUpdateMap(cm, p);

    //TODO this is for testing purposes.
    cm.requireSelect(u, p.getHand(), ImmutableList.of(),
      (us, ih, id) -> {System.out.println("callback " + id); return null;} );

    return cm;
  }

  @Override
  public ClientUpdateMap endActionPhase(User u) {
    assert (current.equals(u));
    actionPhase = false;

    Player p = userPlayers.get(u);
    p.setActions(0);
    p.burnMoney();


    ClientUpdateMap cm = new ClientUpdateMap(this);
    playerUpdateMap(cm, p);
    cm.setPhase(false);



    return cm;
  }

  @Override
  public ClientUpdateMap fullUpdate(User u) {
    ClientUpdateMap cm = new ClientUpdateMap(this);
    playerUpdateMap(cm, userPlayers.get(u));
    return cm;
  }

  public void incrementBuys() {
    Player p = userPlayers.get(current);
    p.incrementBuys();
  }

  public void incrementAdditionalMoney(int adnlMoney) {
    Player p = userPlayers.get(current);
    p.incrementAdditionalMoney(adnlMoney);
  }

  public void incrementActions() {
    Player p = userPlayers.get(current);
    p.incrementActions();
  }

  public void currentDraw(int numCards) {
    Player p = userPlayers.get(current);
    p.draw(numCards);
  }

}
