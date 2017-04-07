package action;

public class Market extends AbstractAction {

  public Market() {
    this.cost = 5;
    this.id = 8;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.currentDraw(1);
    ac.incrementActions();
    ac.incrementBuys();
    ac.incrementAdditionalMoney(1);
  }

}
