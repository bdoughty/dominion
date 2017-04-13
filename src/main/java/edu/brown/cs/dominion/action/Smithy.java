package edu.brown.cs.dominion.action;

public class Smithy extends AbstractAction {

  public Smithy() {
    this.cost = 4;
    this.id = 13;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(3);
  }

}
