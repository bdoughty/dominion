package edu.brown.cs.dominion;

import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.players.Player;

/**
 * Created by henry on 3/22/2017.
 */
public abstract class Card {
  private int id;
  private int cost;
  private transient int victoryPoints;
  private transient int monetaryValue;

  public Card(int id, int cost, int victoryPoints, int monetaryValue) {
    this.id = id;
    this.cost = cost;
    this.victoryPoints = victoryPoints;
    this.monetaryValue = monetaryValue;
  }

  public int getId() {
    return id;
  }

  public int getCost() {
    return cost;
  }

  public int getVictoryPoints() {
    return victoryPoints;
  }

  public int getMonetaryValue() {
    return monetaryValue;
  }

  public void play(Player p) throws NotActionException {

  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof Card)) {
      return false;
    }
    return ((Card) o).getId() == getId();
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(getId());
  }
}
