//package edu.brown.cs.dominion.action;
//
//import edu.brown.cs.dominion.Card;
//import edu.brown.cs.dominion.User;
//import edu.brown.cs.dominion.games.Game;
//import edu.brown.cs.dominion.io.ButtonCallback;
//import edu.brown.cs.dominion.io.send.ButtonCall;
//import edu.brown.cs.dominion.io.send.ClientUpdateMap;
//import edu.brown.cs.dominion.io.send.RequirePlayerAction;
//
//public class Library extends AbstractAction {
//
//  public Library() {
//    super(28, 5);
//  }
//
//  @Override
//  public void play(Game g, ClientUpdateMap cm) {
//    if (g.getCurrentPlayer().getHand().size() < 7) {
//      g.getCurrentPlayer().draw(1);
//
//      Card justDrawn = g.getCurrentPlayer().getHand()
//          .get(g.getCurrentPlayer().getHand().size() - 1);
//
//      ButtonCall b1 = new ButtonCall("Keep " + justDrawn.toString(), new LibraryKeep(g));
//      if (justDrawn instanceof AbstractAction) {
//        ButtonCall b2 = new ButtonCall("Discard " + justDrawn.toString(),
//          new LibraryDiscard(g));
//        cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.buttons(b1, b2));
//      } else {
//        cm.requirePlayerAction(g.getCurrent(), RequirePlayerAction.buttons(b1));
//      }
//    }
//  }
//
//  @Override
//  public String toString() {
//    return "Library";
//  }
//
//}
//
//class LibraryKeep implements ButtonCallback {
//
//  private Game g;
//
//  LibraryKeep(Game g) {
//    this.g = g;
//  }
//
//  @Override
//  public ClientUpdateMap clicked(User u) {
//    if (g.getPlayerFromUser(u).getHand().size() < 7) {
//      g.getPlayerFromUser(u).draw(1);
//
//      Card justDrawn = g.getPlayerFromUser(u).getHand()
//          .get(g.getPlayerFromUser(u).getHand().size() - 1);
//
//      ClientUpdateMap cm = new ClientUpdateMap(g, u);
//
//      g.playerUpdateMap(cm, g.getPlayerFromUser(u));
//
//      ButtonCall b1 = new ButtonCall("Keep " + justDrawn.toString(), new LibraryKeep(g));
//      if (justDrawn instanceof AbstractAction) {
//        ButtonCall b2 = new ButtonCall("Discard " + justDrawn.toString(),
//          new LibraryDiscard(g));
//        cm.requirePlayerAction(u, RequirePlayerAction.buttons(b1, b2));
//      } else {
//        cm.requirePlayerAction(u, RequirePlayerAction.buttons(b1));
//      }
//
//      return cm;
//    } else {
//      return null;
//    }
//  }
//}
//
//class LibraryDiscard implements ButtonCallback {
//
//  private Game g;
//
//  LibraryDiscard(Game g) {
//    this.g = g;
//  }
//
//  @Override
//  public ClientUpdateMap clicked(User u) {
//    g.getPlayerFromUser(u).discard(g.getPlayerFromUser(u).getHand().size() - 1);
//
//    if (g.getPlayerFromUser(u).getHand().size() < 7) {
//      g.getPlayerFromUser(u).draw(1);
//
//      Card justDrawn = g.getPlayerFromUser(u).getHand()
//          .get(g.getPlayerFromUser(u).getHand().size() - 1);
//
//      ClientUpdateMap cm = new ClientUpdateMap(g, u);
//
//      g.playerUpdateMap(cm, g.getPlayerFromUser(u));
//
//      ButtonCall b1 = new ButtonCall("Keep " + justDrawn.toString(), new LibraryKeep(g));
//      if (justDrawn instanceof AbstractAction) {
//        ButtonCall b2 = new ButtonCall("Discard " + justDrawn.toString(),
//          new LibraryDiscard(g));
//        cm.requirePlayerAction(u, RequirePlayerAction.buttons(b1, b2));
//      } else {
//        cm.requirePlayerAction(u, RequirePlayerAction.buttons(b1));
//      }
//
//      return cm;
//    } else {
//      return null;
//    }
//  }
//
//}