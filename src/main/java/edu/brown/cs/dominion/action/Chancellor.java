package edu.brown.cs.dominion.action;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

public class Chancellor extends AbstractAction {

  public Chancellor() {
    super(23, 3);
  }

  @Override
  public void play(Game g, ClientUpdateMap cm) {
    g.getCurrentPlayer().incrementAdditionalMoney(2);
    cm.putButton(g.getCurrent(), "Discard Deck", (usr) -> {
      g.getPlayerFromUser(usr).discardDeck();
      ClientUpdateMap cm1 = new ClientUpdateMap(g, usr);
      g.playerUpdateMap(cm1, g.getPlayerFromUser(usr));
      return cm1;
    });
  }

  @Override
  public String toString() {
    return "Chancellor";
  }

}
