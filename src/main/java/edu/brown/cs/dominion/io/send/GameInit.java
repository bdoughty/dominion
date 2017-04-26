package edu.brown.cs.dominion.io.send;

import java.util.List;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.Pile;
import edu.brown.cs.dominion.User;

/**
 * Created by henry on 4/2/2017.
 */
@Deprecated
public class GameInit {
  private List<User> players;
  private Pile[][] cards;

  public GameInit(List<User> players, List<Card> cds) {
    this.players = players;
    this.cards = new Pile[4][5];
    for (int i = 0; i < 5; i++) {
      cards[1][i] = new Pile(cds.get(i), 10);
      cards[2][i] = new Pile(cds.get(i + 5), 10);
    }
    cards[0][0] = new Pile("Province", 12);
    cards[0][1] = new Pile("Duchy", 12);
    cards[0][2] = new Pile("Estate", 12);
    cards[0][4] = new Pile("Curse", (players.size() - 1) * 10);
    cards[3][2] = new Pile("Copper", 70 - players.size() * 7);
    cards[4][1] = new Pile("Silver", 40);
    cards[5][0] = new Pile("Gold", 30);
  }
}
