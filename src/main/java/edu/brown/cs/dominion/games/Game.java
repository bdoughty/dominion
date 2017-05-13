package edu.brown.cs.dominion.games;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.Board;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoActionsException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserInteruptedException;
import edu.brown.cs.dominion.players.UserPlayer;

/**
 * Created by henry on 4/2/2017.
 */
public class Game extends GameStub {
  private List<Player> allPlayers;
  private Player currentPlayer;
  private Board board;
  private long turnStartTime;

  private boolean turnCanceled = false;

  private int turn = 1;

  private int pt;

  public Game(List<Player> usersTurns, List<Integer> actionCardIds) {
    usersTurns.forEach(u -> u.setGame(this));
    this.allPlayers = new LinkedList<>(usersTurns);
    this.board = new Board(actionCardIds);
    currentPlayer = allPlayers.get(0);

  }

  public void clearDatabase() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String url = "jdbc:sqlite:stats.sqlite3";
    try (Connection conn = DriverManager.getConnection(url);
        Statement stat = conn.createStatement()) {
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
      stat.executeUpdate("DELETE FROM games;");
      stat.executeUpdate("DELETE FROM gameplayers;");
      stat.executeUpdate("DELETE FROM players;");
    } catch (SQLException sqle) {
      System.out.println(sqle.getMessage());
    }
  }

  public void play() {
    while (true) {
      for (pt = 0; pt < allPlayers.size(); pt++) {
        // System.out.println("starting turn of player " + pt);
        Player p = allPlayers.get(pt);
        if (playTurn(p)) {
          pt++;
        }
        if (board.gameHasEnded() || allPlayers.size() == 0) {
          break;
        }
      }
      if (board.gameHasEnded() || allPlayers.size() == 0) {
        break;
      }
      turn++;
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
        sendMessage(p.getName() + " bought " + c.toString() + ".");
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
    // try {
    // updateDatabase();
    // } catch (ClassNotFoundException e) {
    // System.out.println(e.getMessage());
    // }
  }

  public Set<Integer> winners() {
    Map<Integer, Integer> vps = getVictoryPointMap();
    int maxScore = Collections.max(vps.values());
    return vps.keySet().stream().filter((key) -> {
      return vps.get(key) == maxScore;
    }).collect(Collectors.toSet());
  }

  public void updateDatabase() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String url = "jdbc:sqlite:stats.sqlite3";
    try (Connection conn = DriverManager.getConnection(url);
        Statement stat = conn.createStatement();
        PreparedStatement prep1 = conn.prepareStatement(
            "INSERT INTO games (ID, NUMPLAYERS, NUMTURNS) VALUES (?, ?, ?);");
        PreparedStatement prep2 = conn.prepareStatement(
            "INSERT INTO players (ID, DECKSIZE, NUMVPS, NUMACTS, NUMMONEY, TOTVP, TOTMONEY, POSN) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        PreparedStatement prep3 = conn.prepareStatement(
            "INSERT INTO gameplayers (GAMEID, PLAYERID) VALUES (?, ?);")) {

      stat.executeUpdate("PRAGMA foreign_keys = ON;");

      prep1.setInt(1, getId());
      prep1.setInt(2, allPlayers.size());
      prep1.setInt(3, turn);
      prep1.executeUpdate();

      for (Player p : allPlayers) {
        prep2.setInt(1, p.getId());
        prep2.setInt(2,
            p.getDeck().size() + p.getDiscard().size() + p.getHand().size());
        prep2.setInt(3, p.getNumVps());
        prep2.setInt(4, p.getNumActs());
        prep2.setInt(5, p.getNumMoney());
        prep2.setInt(6, p.scoreDeck());
        prep2.setInt(7, p.countMoney());
        prep2.setInt(8, allPlayers.indexOf(p) + 1);
        prep2.executeUpdate();

        prep3.setInt(1, getId());
        prep3.setInt(2, p.getId());
        prep3.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean playTurn(Player p) {
    turnStartTime = System.currentTimeMillis();
    sendMessage(p.getName() + " began their turn.");
    Thread turnEnder = new Thread(() -> {
      try {
        Thread.sleep(61000);
        cancelTurn();
        System.out.println("ending turn");
        pt--;
      } catch (InterruptedException ignored) {
      }
    });
    turnEnder.start();
    currentPlayer = p;
    p.newTurn();
    if (!turnCanceled) {
      doActions(p);
      // System.out.println("canceled1");
    } else {
      // System.out.println("canceled2");
    }
    if (!turnCanceled) {
      buyPhase(p);
      // System.out.println("canceled3");
    } else {
      // System.out.println("canceled4");
    }
    p.endTurn();
    sendMessage(p.getName() + " ended their turn.");
    boolean c = turnCanceled;
    turnCanceled = false;
    turnEnder.interrupt();
    return c;
  }

  public void cancelTurn() {
    turnCanceled = true;
    if (currentPlayer instanceof UserPlayer) {
      ((UserPlayer) currentPlayer).cancelAll();
    }
  }

  public int getCurrentPlayerId() {
    return currentPlayer.getId();
  }

  public void doActions(Player p) {
    int loc;
    try {
      while (-1 != (loc = p.playHandAction()) && !turnCanceled) {
        try {
          Card c = p.play(loc);
          c.play(p);
          sendMessage(p.getName() + " played " + c.toString() + ".");
        } catch (NoActionsException | NotActionException ignored) {
          p.endActionPhase();
          return;
        }
      }
    } catch (UserInteruptedException ignored) {
    }
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
          sendMessage(p.getName() + " gained a Curse.");
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
    if (p == currentPlayer) {
      cancelTurn();
    }
    allPlayers.remove(p);
  }

  public int getTimeLeftOnTurn() {
    return (int) (61000 - System.currentTimeMillis() + turnStartTime);
  }

  public void addPlayer(Player p) {
    allPlayers.add(p);
  }

  public void sendMessage(String s) {
  }
}
