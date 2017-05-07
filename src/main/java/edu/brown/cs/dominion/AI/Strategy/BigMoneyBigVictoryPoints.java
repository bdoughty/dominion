package edu.brown.cs.dominion.AI.Strategy;

import java.util.Arrays;
import java.util.List;

import edu.brown.cs.dominion.players.Player;

public class BigMoneyBigVictoryPoints implements Strategy {
  private static List<Integer> BUY_PREFERENCES = Arrays.asList(5, 2, 4, 1),
      DISCARD_PREFERENCES = Arrays.asList(6, 3, 4, 5, 0, 1, 2);

  @Override
  public List<Integer> getDiscardPreferences(Player who) {
    return DISCARD_PREFERENCES;
  }

  @Override
  public List<Integer> getBuyPreferences(Player who) {
    return BUY_PREFERENCES;
  }

  @Override
  public int trashForChapel(Player who) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public int trashForRemodel(Player who) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public int playThroneRoom(Player who) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public int playAction(Player who) {
    return -1;
  }
}
