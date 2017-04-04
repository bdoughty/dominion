package action;

public class Village extends AbstractAction {

  public Village() {
    this.cost = 3;
    this.id = 14;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementActions();
    ac.incrementActions();
    ac.currentDraw(1);
  }

}
