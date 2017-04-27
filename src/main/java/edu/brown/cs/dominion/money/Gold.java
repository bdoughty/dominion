package edu.brown.cs.dominion.money;

public class Gold extends AbstractMoney {

  public Gold() {
    super(2, 6, 3);
  }

  @Override
  public String toString() {
    return "Gold";
  }

}
