package edu.brown.cs.dominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetMine;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

/**
 * Use random play ai to test game functionality. Created by henry on 5/7/2017.
 */
public class AITest {
  // private static Random r = new Random();

  public static void main(String[] args) {
    double sampleSize = 500.0;
    int numAis = 2;
    double[] totalVPs = new double[numAis];
    for (int i = 0; i < numAis; i++) {
      totalVPs[i] = 0.0;
    }
    Map<Integer, Integer> idsToPositions = new HashMap<>();
    for (int i = 0; i < sampleSize; i++) {
      List<Player> players = new ArrayList<>();
      for (int j = 0; j < numAis; j++) {
        Player next = new AIPlayer(new NeuralNetMine(j));
        idsToPositions.put(next.getId(), j);
        players.add(next);
      }

      Collections.shuffle(players);

      Game g = new Game(players, get10cards());
      g.play();
      for (Entry<Integer, Integer> e : g.getVictoryPointMap().entrySet()) {
        totalVPs[idsToPositions.get(e.getKey())] =
            totalVPs[idsToPositions.get(e.getKey())] + e.getValue();
      }
      // for (int w : g.winners()) {
      // totalVPs[idsToPositions.get(w)] = totalVPs[idsToPositions.get(w)] + 1;
      // }

      if ((int) (i * 100.0 / sampleSize) > (int) ((i - 1) * 100.0
          / sampleSize)) {
        System.out.println((int) (i * 100.0 / sampleSize) + "%");
      }
    }
    double[] averages = Arrays.copyOf(totalVPs, totalVPs.length);
    for (int i = 0; i < averages.length; i++) {
      averages[i] = totalVPs[i] / sampleSize;
    }

    System.out.println(Arrays.toString(averages));

    // double sampleSize = 50000.0;
    // for (int i = 0; i < sampleSize; i++) {
    // List<Player> players = new ArrayList<>();
    // int numPlayers = r.nextInt(3) + 2;
    // while (players.size() < numPlayers) {
    // players.add(new AIPlayer(new DumbStrategy()));
    // }
    //
    // Game g = new Game(players, get10cards());
    // g.play();
    //
    // if ((int) (i * 100.0 / sampleSize) > (int) ((i - 1) * 100.0
    // / sampleSize)) {
    // System.out.println((int) (i * 100.0 / sampleSize) + "%");
    // }
    // }
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
