package edu.brown.cs.dominion.money;

public class Silver extends AbstractMoney {

  public Silver() {
    super(1, 3, 2);
  }

  @Override
  public String toString() {
    return "Silver";
  }

}
