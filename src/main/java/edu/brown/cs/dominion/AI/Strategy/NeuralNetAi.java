package edu.brown.cs.dominion.AI.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.AI.neuralNetwork.NeuralNetwork;
import edu.brown.cs.dominion.AI.neuralNetwork.NeuralNetworkIO;
import edu.brown.cs.dominion.players.Player;

public class NeuralNetAi implements Strategy {
  private static List<Integer> DISCARD_PREFERENCES =
      Arrays.asList(6, 3, 4, 5, 0, 1, 2);

  private static NeuralNetwork nn =
      NeuralNetworkIO.load("src/main/resources/best.nn");

  @Override
  public List<Integer> getDiscardPreferences(Player who) {
    return DISCARD_PREFERENCES;
  }

  @Override
  public List<Integer> getBuyPreferences(Player who) {
    double[] possessions = new double[] {0, 0, 0, 0, 0, 0};
    List<Card> allCards = new ArrayList<>(who.getDeck());
    allCards.addAll(who.getHand());
    allCards.addAll(who.getDiscard());
    allCards.forEach((card) -> {
      if (card.getId() < 6) {
        possessions[card.getId()] = possessions[card.getId()] + 1;
      }
    });

    double[] results = nn.run(possessions);
    assert (results.length == 6);

    Map<Integer, Double> map = new HashMap<>();
    for (int i = 0; i < 6; i++) {
      map.put(i, results[i]);
    }
    List<Integer> out = new ArrayList<>();
    if (!allCards.stream().map(Card::getId).collect(Collectors.toList())
        .contains(10)) {
      out.add(10);
    }

    while (map.size() > 0) {
      double max = Collections.max(map.values());
      int indexOfMax = getKeyFromFirstOccuranceOfValue(map, max);
      out.add(indexOfMax);
      map.remove(indexOfMax);
    }

    System.out.println("out: " + Arrays.toString(out.toArray()));

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
    return -1;
  }

  @Override
  public int trashForRemodel(Player who) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public int playThroneRoom(Player who) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public int playAction(Player who) {
    // TODO Auto-generated method stub
    return who.getHand().stream().map(Card::getId).collect(Collectors.toList())
        .indexOf(10);
  }

}
