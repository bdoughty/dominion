package edu.brown.cs.dominion.action;

public class Militia extends AbstractAction {

  public Militia() {
    this.cost = 4;
    this.id = 9;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementAdditionalMoney(2);
    ac.militia();
  }

}
