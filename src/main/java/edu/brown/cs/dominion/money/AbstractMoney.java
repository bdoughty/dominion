package edu.brown.cs.dominion.money;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.players.Player;

public abstract class AbstractMoney extends Card {

  public AbstractMoney(int id, int cost, int monetaryValue) {
    super(id, cost, 0, monetaryValue);
  }

  @Override
  public void play(Player p) throws NotActionException {
    throw new NotActionException("can't play money");
  }

}
