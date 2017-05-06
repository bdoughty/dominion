package edu.brown.cs.dominion.AI.Strategy;


import java.util.List;

import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

public interface Strategy
{
    int playAction(Game g, Player who);

    int discard(Game g, Player who);

    int buy(int money, Game g, Player who);

    static List<Integer> buyable(int money, Game g)
    {
        return g.getBoard().getCardsUnderValue(money);
    }
}
