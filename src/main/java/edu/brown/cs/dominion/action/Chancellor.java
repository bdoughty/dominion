//package edu.brown.cs.dominion.action;
//
//import edu.brown.cs.dominion.games.Game;
//import edu.brown.cs.dominion.io.send.ButtonCall;
//import edu.brown.cs.dominion.io.send.ClientUpdateMap;
//import edu.brown.cs.dominion.io.send.RequirePlayerAction;
//
//public class Chancellor extends AbstractAction {
//
//  public Chancellor() {
//    super(23, 3);
//  }
//
//  @Override
//  public void play(Game g, ClientUpdateMap cm) {
//    g.getCurrentPlayer().incrementAdditionalMoney(2);
//
//    ButtonCall b1 = new ButtonCall("Discard Deck", (usr) -> {
//      g.getPlayerFromUser(usr).discardDeck();
//      ClientUpdateMap cm1 = new ClientUpdateMap(g, usr);
//      g.playerUpdateMap(cm1, g.getPlayerFromUser(usr));
//      return cm1;
//    });
//    ButtonCall b2 =  new ButtonCall("Don't Discard Deck", (usr) -> null);
//    cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.buttons(b1, b2));
//  }
//
//  @Override
//  public String toString() {
//    return "Chancellor";
//  }
//
//}
