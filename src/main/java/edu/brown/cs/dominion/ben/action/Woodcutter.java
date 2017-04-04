package action;

public class Woodcutter extends AbstractAction {

  public Woodcutter() {
    this.cost = 3;
    this.id = 15;
  }

  @Override
  public void play(ActionCenter ac) {
    ac.incrementBuys();
    ac.incrementAdditionalMoney(2);
  }

}
