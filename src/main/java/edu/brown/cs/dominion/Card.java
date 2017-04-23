package edu.brown.cs.dominion;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.gameutil.NotActionException;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

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

  public static Card getCardFromId(int id) {
    // TODO this
    return null;
  }

  public static Card getCardFromName(String name) {
    // TODO this
    return null;
  }

  public void play(Game g, ClientUpdateMap cm) throws NotActionException {

  }
}
