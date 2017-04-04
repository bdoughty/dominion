package action;

public class Moat extends AbstractAction {

  public Moat() {
    this.cost = 2;
    this.id = 11;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(2);
  }

}
