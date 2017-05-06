package edu.brown.cs.dominion.games;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.GameChat;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.gameutil.EmptyPileException;
import edu.brown.cs.dominion.gameutil.NoPileException;
import edu.brown.cs.dominion.gameutil.TooExpensiveException;
import edu.brown.cs.dominion.io.MessageListener;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.players.Player;
import edu.brown.cs.dominion.players.UserPlayer;
import edu.brown.cs.dominion.io.send.MessageType;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by henry on 5/5/2017.
 */
public class UserGame extends Game {
  private static Gson GSON = new Gson();

  private List<User> allUsers;
  private Websocket gameSocket;
  private GameChat gc;
  private GameManager gm;
  public UserGame(List<User> usersTurns,
                  List<Integer> actionCardIds,
                  Websocket gameSocket,
                  GameManager gm) {
    super(usersTurns.stream().map(u -> new UserPlayer(u, gameSocket)).collect
      (Collectors.toList()), actionCardIds);
    allUsers = usersTurns;
    getPlayers().forEach(p -> p.setGame(this));
    gc = new GameChat(gameSocket, allUsers);
    this.gameSocket = gameSocket;
    this.gm = gm;
  }

  public Map<User, UserPlayer> getUserPlayerMap () {
    Map<User, UserPlayer> userPlayers = new HashMap<>();
    getPlayers().forEach(p -> {
        if (p instanceof UserPlayer) {
          UserPlayer u = (UserPlayer) p;
          userPlayers.put(u.getUser(), u);
        }
      });
    return userPlayers;
  }

  public void sendChat(User u, String message){
    gc.send(u, message);
  }

  public void sendAll(MessageType type, String message){
    for (User u : allUsers) {
      gameSocket.send(u, type, message);
    }
  }

  @Override
  public void playTurn(Player p) {
    sendAll(MessageType.TURN, Integer.toString(p.getId()));
    super.playTurn(p);
    sendAll(MessageType.VICTORY_POINTS, GSON.toJson(getVictoryPointMap()));
  }

  @Override
  public Card gain(int id) throws EmptyPileException, NoPileException {
    Card c = super.gain(id);
    sendBoard();
    return c;
  }

  @Override
  public Card buyCard(int buyId, int money) throws NoPileException, TooExpensiveException, EmptyPileException {
    Card c = super.buyCard(buyId, money);
    sendBoard();
    return c;
  }


  private void sendBoard() {
    sendAll(MessageType.BOARD, GSON.toJson(getBoard()));
  }

  public void sendBoard(Session s){
    gameSocket.send(s, MessageType.BOARD, GSON.toJson(getBoard()));
  }

  @Override
  public void win(){
    super.win();
    sendAll(MessageType.WINNER, GSON.toJson(getVictoryPointMap()));
    gm.finish(allUsers);
  }

  public void sendTurn(Session s) {
    gameSocket.send(s, MessageType.TURN, getCurrentPlayerId());
  }

  public void sendAllHandSize(Player p) {
    JsonObject handSize = new JsonObject();
    handSize.addProperty("id", p.getId());
    handSize.addProperty("cards", p.getHand().size());
    sendAll(MessageType.HAND_NUM, GSON.toJson(handSize));
  }
}
