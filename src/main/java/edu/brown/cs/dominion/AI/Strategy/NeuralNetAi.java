package edu.brown.cs.dominion.AI.Strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.neuralNetwork.NeuralNetwork;
import edu.brown.cs.dominion.neuralNetwork.NeuralNetworkIO;
import edu.brown.cs.dominion.players.Player;

public class NeuralNetAi implements Strategy {
  private static NeuralNetwork nn =
      NeuralNetworkIO.load("src/main/resources/best.nn");

  @Override
  public List<Integer> getDiscardPreferences(Player who) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Integer> getBuyPreferences(Player who) {
    double[] possessions = new double[] {0, 0, 0, 0, 0, 0};
    List<Card> allCards = new ArrayList<>(who.getDeck());
    allCards.addAll(who.getHand());
    allCards.addAll(who.getDiscard());
    allCards.forEach((card) -> {
      possessions[card.getId()] = possessions[card.getId()] + 1;
    });

    double[] results = nn.run(possessions);
    assert (results.length == 6);

    Map<Integer, Double> map = new HashMap<>();
    for (int i = 0; i < 6; i++) {
      map.put(i, results[i]);
    }
    List<Integer> out = new ArrayList<>();

    while (map.size() > 0) {
      double max = Collections.max(map.values());
      int indexOfMax = getKeyFromFirstOccuranceOfValue(map, max);
      out.add(indexOfMax);
      map.remove(indexOfMax);
    }

    System.out.println(1 / 0); // because I'm sure this code is wrong, so I'll
                               // come back later and fix it? hopefully?

    return out;
  }

  private static int getKeyFromFirstOccuranceOfValue(Map<Integer, Double> m,
      double v) {
    for (Entry<Integer, Double> e : m.entrySet()) {
      if (e.getValue() == v) {
        return e.getKey();
      }
    }
    return -1;
  }

  @Override
  public int trashForChapel(Player who) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int trashForRemodel(Player who) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int playThroneRoom(Player who) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int playAction(Player who) {
    // TODO Auto-generated method stub
    return 0;
  }

}
