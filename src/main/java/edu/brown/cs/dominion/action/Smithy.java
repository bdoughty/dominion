package edu.brown.cs.dominion.action;

public class Smithy extends AbstractAction {

  public Smithy() {
    super(13, 4);
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(3);
  }

}
