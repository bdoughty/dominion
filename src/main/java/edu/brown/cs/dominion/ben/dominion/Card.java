package dominion;

import action.ActionCenter;

public interface Card {

  void play(ActionCenter ac);

  int getCost();

  int getValue();

  int getPoints();

  int getId();

}
