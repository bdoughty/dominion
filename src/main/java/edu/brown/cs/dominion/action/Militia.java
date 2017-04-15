package edu.brown.cs.dominion.action;

public class Militia extends AbstractAction {

  public Militia() {
    super(9, 4);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementAdditionalMoney(2);
    ac.militia();
  }

}
