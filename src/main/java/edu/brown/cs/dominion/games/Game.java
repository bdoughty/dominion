package edu.brown.cs.dominion.games;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.GameChat;
import edu.brown.cs.dominion.User;
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

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub implements GameEventListener {
  private List<User> allUsers;
  private Queue<User> usersTurns;
  private User current;
  private Map<User, Player> userPlayers;
  private Board board;
  private static File spambot = new File("src/main/resources/trash_talk.txt");

  private GameChat gc;

  private boolean actionPhase = true;

  public Game(List<User> usersTurns, List<Integer> actionCardIds,
      Websocket ws) {
    userPlayers = new HashMap<>();
    usersTurns.forEach(u -> userPlayers.put(u, new Player()));
    this.allUsers = new LinkedList<>(usersTurns);
    this.usersTurns = new LinkedList<>(usersTurns);
    this.current = this.usersTurns.poll();
    userPlayers.get(current).newTurn();
    this.board = new Board(actionCardIds);

    gc = new GameChat(ws, usersTurns);
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
  public ClientUpdateMap endBuyPhase(User u, List<Integer> toBuy) {
    if (current.getId() != u.getId()) {
      return null;
    }

    sendServerMessage("Hey " + u.getName() + ", " + getRandomSpamMessage());

    actionPhase = true;

    Player p = userPlayers.get(u);
    int money = p.getMoney();

    for (int buyId : toBuy) {
      try {
        Card c = board.buyCard(buyId, money);
        p.buyCard(c);
        money -= c.getCost();
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
      int highScore =
          userPlayers.get(Collections.max(allUsers, (User one, User two) -> {
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
  public ClientUpdateMap startTurn(User u) {
    userPlayers.get(current).newTurn();
    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    playerUpdateMap(cm, getCurrentPlayer());
    return cm;
  }

  @Override
  public ClientUpdateMap doAction(User u, int LocInHand) {
    if (current.getId() != u.getId()) {
      return null;
    }

    assert (current.equals(u));
    Player p = userPlayers.get(u);

    ClientUpdateMap cm = new ClientUpdateMap(this, u);

    try {
      Card c = p.play(LocInHand);
      c.play(this, cm);
      playerUpdateMap(cm, p);
    } catch (NotActionException nae) {
      System.out.println(nae.getMessage());
    } catch (NoActionsException nae) {
      System.out.println(nae.getMessage());
    }

    return cm;
  }

  @Override
  public ClientUpdateMap endActionPhase(User u) {
    if (current.getId() != u.getId()) {
      return null;
    }

    actionPhase = false;

    Player p = userPlayers.get(u);
    p.setActions(0);
    p.burnHand();

    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    playerUpdateMap(cm, p);
    cm.setPhase(false);

    return cm;
  }

  @Override
  public ClientUpdateMap fullUpdate(User u) {
    ClientUpdateMap cm = new ClientUpdateMap(this, u);
    cm.piles(board);
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

  public void trash(Card c) {
    board.trashCard(c);
  }

  public Card gain(int id) throws EmptyPileException, NoPileException {
    return board.gainCard(id);
  }

  public void sendMessage(User u, String s) {
    gc.send(u, s);
  }

  public void sendServerMessage(String s) {
    gc.serverSend(s);
  }

  private static String getRandomSpamMessage() {
    try (BufferedReader br = new BufferedReader(new FileReader(spambot))) {
      String out = "go rek urself!!!";
      if (br.ready()) {
        String line = br.readLine();
        int i = 1;
        while (line != null) {
          if (line.equals("")) {
            line = br.readLine();
            continue;
          }
          if (Math.random() * i < 1) {
            out = line;
          }
          line = br.readLine();
          i++;
        }
      }
      return out;
    } catch (Exception e) {
      e.printStackTrace();
      return "you literally broke the spambot you're so bad!";
    }
  }
}
