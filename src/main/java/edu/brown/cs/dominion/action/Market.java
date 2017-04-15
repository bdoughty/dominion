package edu.brown.cs.dominion.action;

public class Market extends AbstractAction {

  public Market() {
    super(8, 5);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(1);
    ac.incrementActions();
    ac.incrementBuys();
    ac.incrementAdditionalMoney(1);
  }

}
