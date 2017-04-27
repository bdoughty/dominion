package edu.brown.cs.dominion.money;

public class Copper extends AbstractMoney {

  public Copper() {
    super(0, 0, 1);
  }

  @Override
  public String toString() {
    return "Copper";
  }

}
