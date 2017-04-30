package edu.brown.cs.dominion.games;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.GameChat;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.AI.TrashTalker;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.gameutil.Player;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.io.GameEventListener;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

import javax.jws.soap.SOAPBinding;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub implements GameEventListener {
  private List<User> allUsers;
  private Queue<User> usersTurns;
  private User current;
  private Map<User, Player> userPlayers;
  private Board board;
  private static TrashTalker spambot = new TrashTalker();

  private GameChat gc;

  private boolean actionPhase = true;

  private GameManager gm;

  public Game(List<User> usersTurns, List<Integer> actionCardIds,
      Websocket ws, GameManager gm) {
    userPlayers = new HashMap<>();
    usersTurns.forEach(u -> userPlayers.put(u, new Player()));
    this.allUsers = new LinkedList<>(usersTurns);
    this.usersTurns = new LinkedList<>(usersTurns);
    this.current = this.usersTurns.poll();
    userPlayers.get(current).newTurn();
    this.board = new Board(actionCardIds);
    gc = new GameChat(ws, usersTurns);
    spambot.addGame(this);
    this.gm = gm;
    startTurn(current);
  }

  public User getCurrent() {
    return current;
  }

  public Player getCurrentPlayer() {
    return userPlayers.get(current);
  }

  public Player getPlayerFromUser(User u) {
    return userPlayers.get(u);
  }

  public List<User> getAllUsers() {
    return allUsers;
  }

  public Board getBoard() {
    return board;
  }

  public void playerUpdateMap(ClientUpdateMap cm, Player p) {
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
  public void endBuyPhase(User u, List<Integer> toBuy) {
    if (current.getId() != u.getId()) {
      return;
    }

    actionPhase = true;

    Player p = userPlayers.get(u);
    int money = p.getMoney();

    for (int buyId : toBuy) {
      try {
        Card c = board.buyCard(buyId, money);
        p.buyCard(c);
        money -= c.getCost();
        sendServerMessage(u.getName() + " bought " + c.toString() + ".");
      } catch (TooExpensiveException tee) {
        System.out.println(tee.getMessage());
      } catch (EmptyPileException epe) {
        System.out.println(epe.getMessage());
      } catch (NoPileException npe) {
        System.out.println(npe.getMessage());
      }
    }

    p.endTurn();
    usersTurns.add(current);
    current = usersTurns.poll();

    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    playerUpdateMap(cm, p);
    cm.turn(current.getId());
    cm.piles(board);

    if (board.gameHasEnded()) {
      cm.winner(getVictoryPointMap());
    }
    cm.victoryPoints(getVictoryPointMap());

    sendServerMessage(u.getName() + " ended their turn.");

    gm.sendClientUpdateMap(cm);


    //TODO THIS IS SOOOOOOO BADDDDD SOOO BADDDDDA
    //try {
     // Thread.sleep(1000);
    //} catch (InterruptedException e) {
    //  e.printStackTrace();
    //}
    startTurn(current);
  }

  @Override
  public void startTurn(User u) {
    userPlayers.get(current).newTurn();
    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    playerUpdateMap(cm, getCurrentPlayer());
    sendServerMessage(u.getName() + " began their turn.");
    if(u instanceof AIPlayer){
      ((AIPlayer) u).play(this);
    }
    gm.sendClientUpdateMap(cm);
  }

  @Override
  public void doAction(User u, int LocInHand) {
    if (current.getId() != u.getId()) {
      return;
    }

    assert (current.equals(u));
    Player p = userPlayers.get(u);

    ClientUpdateMap cm = new ClientUpdateMap(this, u);

    try {
      Card c = p.play(LocInHand);
      sendServerMessage(u.getName() + " played " + c.toString() + ".");
      c.play(this, cm);
      playerUpdateMap(cm, p);
    } catch (NotActionException nae) {
      System.out.println(nae.getMessage());
    } catch (NoActionsException nae) {
      System.out.println(nae.getMessage());
    }

    gm.sendClientUpdateMap(cm);
  }

  @Override
  public void endActionPhase(User u) {
    if (current.getId() != u.getId()) {
      return;
    }

    actionPhase = false;

    Player p = userPlayers.get(u);
    p.setActions(0);
    p.burnHand();

    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    playerUpdateMap(cm, p);
    cm.setPhase(false);

    gm.sendClientUpdateMap(cm);
  }

  @Override
  public ClientUpdateMap fullUpdate(User u) {
    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    cm.piles(board);
    cm.turn(current.getId());
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

  public void othersDraw(int numCards) {
    for (User u : usersTurns) {
      getPlayerFromUser(u).draw(numCards);
    }
  }

  public void trash(Card c) {
    board.trashCard(c);
  }

  public Card gain(int id) throws EmptyPileException, NoPileException {
    return board.gainCard(id);
  }

  public void othersGainCurse() {
    for (User u : usersTurns) {
      try {
        getPlayerFromUser(u).gain(board.gainCard(6), false, false);
      } catch (EmptyPileException | NoPileException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void sendMessage(User u, String s) {
    gc.send(u, s);
  }

  public void sendServerMessage(String s) {
    gc.serverSend(s);
  }

  public void sendServerMessageToUser(String s, User u) {
    gc.serverSendToUser(s, u);
  }

  public void sendSpamMessage(String s) {
    gc.spambotSend(s);
  }

  public Map<Integer, Integer> getVictoryPointMap(){
    Map<Integer, Integer> vps = new HashMap<>();
    allUsers.forEach(u -> vps.put(u.getId(), getPlayerFromUser(u).scoreDeck()));
    return vps;
  }

  public void notifyPlayer() {
    //TODO
  }

  public void notifyGame() {
    //TODO
  }

  public void removeUser(User u) {
    if (u == current && allUsers.size() > 1) {
      current = usersTurns.poll();
      ClientUpdateMap cm = new ClientUpdateMap(this, u);
      cm.turn(current.getId());
      cm.piles(board);
      gm.sendClientUpdateMap(cm);
    } else {
      usersTurns.remove(u);
    }
    userPlayers.remove(u);
    allUsers.remove(u);
    gc.removeUser(u);
  }
}
