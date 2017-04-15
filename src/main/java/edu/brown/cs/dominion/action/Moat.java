package edu.brown.cs.dominion.action;

public class Moat extends AbstractAction {

  public Moat() {
    super(11, 2);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(2);
  }

}
