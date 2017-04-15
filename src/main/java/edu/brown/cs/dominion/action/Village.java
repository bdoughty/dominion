package edu.brown.cs.dominion.action;

public class Village extends AbstractAction {

  public Village() {
    super(14, 3);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementActions();
    ac.incrementActions();
    ac.currentDraw(1);
  }

}
