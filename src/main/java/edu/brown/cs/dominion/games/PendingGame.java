package edu.brown.cs.dominion.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.dominion.AI.UserAIPlayer;
import edu.brown.cs.dominion.AI.Strategy.BigMoneyBigVictoryPoints;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetAi;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetMine;
import edu.brown.cs.dominion.io.User;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.MessageType;

/**
 * Created by henry on 4/15/2017.
 */
public class PendingGame extends GameStub {
  private static Gson GSON = new Gson();

  private List<User> users;
  private int maxusers;
  private int[] actioncardids;
  private String name;
  private List<String> aiTypes;

  public PendingGame(String name, int maxUsers, int[] actionCardIds) {
    this.name = name;
    this.maxusers = maxUsers;
    this.actioncardids = actionCardIds;
    this.users = new LinkedList<>();
    this.aiTypes = Collections.emptyList();
  }

  public PendingGame(String name, int numPlayers, int[] crds,
      List<String> aiTypes) {
    this(name, numPlayers - aiTypes.size(), crds);
    this.aiTypes = aiTypes;
  }

  public JsonObject toJson() {
    JsonObject main = new JsonObject();
    main.addProperty("maxusers", maxusers + aiTypes.size());

    List<String> playerNames = new ArrayList<>();
    users.forEach(u -> playerNames.add(u.getName()));
    aiTypes.forEach(u -> playerNames.add(u));
    JsonElement je = GSON.toJsonTree(playerNames);
    main.addProperty("name", name);
    main.add("names", je);
    main.add("actioncardids", GSON.toJsonTree(actioncardids));
    main.addProperty("maxusers", maxusers + aiTypes.size());
    main.addProperty("id", getId());
    return main;
  }

  public boolean addUser(User u) {
    if (users.size() == maxusers || users.contains(u)) {
      return false;
    } else {
      users.add(u);
      return true;
    }
  }

  public boolean full() {
    return users.size() >= maxusers;
  }

  public void removeUser(User u) {
    users.remove(u);
  }

  // TODO
  public UserGame convertAndRedirect(Websocket ws, GameManager gameManager) {
    List<Integer> cards = new LinkedList<>();
    for (int i : actioncardids) {
      cards.add(i);
    }
    gameManager.removePendingByUser(users);
    UserGame g = new UserGame(users, cards, gameManager.web(), gameManager);
    users.forEach(u -> ws.send(u, MessageType.REDIRECT, "game"));
    aiTypes.forEach(type -> {
      switch (type) {
        case "Enhanced Neural Net":
          g.addPlayer(new UserAIPlayer(g, new NeuralNetMine(1)));
          return;
        case "Medium":
          g.addPlayer(new UserAIPlayer(g, new BigMoneyBigVictoryPoints()));
          return;
        case "Dumb":
          g.addPlayer(new UserAIPlayer(g, new DumbStrategy()));
          return;
        case "Neural Net":
          g.addPlayer(new UserAIPlayer(g, new NeuralNetAi()));
          return;
      }
    });
    new Thread(g::play).start();
    return g;
  }

  public List<User> getUsers() {
    return users;
  }
}
