package edu.brown.cs.dominion.action;

public class Woodcutter extends AbstractAction {

  public Woodcutter() {
    super(15, 3);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementBuys();
    ac.incrementAdditionalMoney(2);
  }

}
