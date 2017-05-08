package edu.brown.cs.dominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetAi;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetMine;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

/**
 * Use random play ai to test game functionality. Created by henry on 5/7/2017.
 */
public class AITest {
  public static void main(String[] args) {
    int[] numWon = new int[] {0, 0, 0, 0, 0};
    Map<Integer, Integer> idsToPositions = new HashMap<>();
    for (int i = 0; i < 20000; i++) {
      Player zero = new AIPlayer(new NeuralNetAi()),
          one = new AIPlayer(new NeuralNetMine()),
          two = new AIPlayer(new NeuralNetAi()),
          three = new AIPlayer(new NeuralNetMine()),
          four = new AIPlayer(new NeuralNetAi());
      idsToPositions.put(zero.getId(), 0);
      idsToPositions.put(one.getId(), 1);
      idsToPositions.put(two.getId(), 2);
      idsToPositions.put(three.getId(), 3);
      idsToPositions.put(four.getId(), 4);

      Game g =
          new Game(ImmutableList.of(zero, one, two, three, four), get10cards());
      g.play();
      for (int w : g.win()) {
        numWon[idsToPositions.get(w)] = numWon[idsToPositions.get(w)] + 1;
      }
    }
    System.out.println(Arrays.toString(numWon));

    // NeuralNetwork nn = NeuralNetworkIO.load("src/main/resources/best.nn");
    // System.out
    // .println(Arrays.toString(nn.run(new double[] {0.7, 0, 0, 0.3, 0, 0})));
  }

  private static List<Integer> get10cards() {
    List<Integer> a = new ArrayList<>();
    while (a.size() < 10) {
      int n = (int) (Math.random() * 22 + 7);
      if (!a.contains(n) && n != 22 && n != 9 && n != 20) {
        a.add(n);
      }
    }
    return a;
  }
}
