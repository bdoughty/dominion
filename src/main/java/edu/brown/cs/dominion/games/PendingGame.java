package edu.brown.cs.dominion.games;

import edu.brown.cs.dominion.User;

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

  public void removeUser(User u) {
    users.remove(u);
  }
}
