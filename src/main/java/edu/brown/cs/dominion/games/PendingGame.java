package edu.brown.cs.dominion.games;

import com.google.common.collect.Lists;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.Websocket;
import edu.brown.cs.dominion.io.send.MessageType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by henry on 4/15/2017.
 */
public class PendingGame extends GameStub{
  private List<User> users;
  private int maxusers;
  private int[] actioncardids;
  private String name;

  public PendingGame(String name, int maxUsers, int[] actionCardIds) {
    this.name = name;
    this.maxusers = maxUsers;
    this.actioncardids = actionCardIds;
    this.users = new LinkedList<>();
  }

  public boolean addUser(User u){
    if(users.size() == maxusers || users.contains(u)) {
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

  //TODO
  public UserGame convertAndRedirect(Websocket ws, GameManager gameManager){
    List<Integer> cards = new LinkedList<>();
    for(int i : actioncardids){
      cards.add(i);
    }
    gameManager.removePendingByUser(users);
    UserGame g = new UserGame(users, cards, gameManager.web(), gameManager);
    users.forEach(u -> ws.send(u, MessageType.REDIRECT, "game"));
    new Thread(g::play).start();
    return g;
  }

  public List<User> getUsers(){
    return users;
  }
}
