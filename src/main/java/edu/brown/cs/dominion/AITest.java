package edu.brown.cs.dominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.NeuralNetMine;
import edu.brown.cs.dominion.games.Game;
import edu.brown.cs.dominion.players.Player;

/**
 * Use random play ai to test game functionality. Created by henry on 5/7/2017.
 */
public class AITest {
  private static Random r = new Random();

  public static void main(String[] args) {
    // double sampleSize = 50000.0;
    // int[] numWon = new int[] {0, 0, 0, 0};
    // Map<Integer, Integer> idsToPositions = new HashMap<>();
    // for (int i = 0; i < sampleSize; i++) {
    // Player zero = new AIPlayer(new NeuralNetMine(0)),
    // one = new AIPlayer(new NeuralNetMine(1)),
    // two = new AIPlayer(new NeuralNetMine(2)),
    // three = new AIPlayer(new NeuralNetMine(3));
    // idsToPositions.put(zero.getId(), 0);
    // idsToPositions.put(one.getId(), 1);
    // idsToPositions.put(two.getId(), 2);
    // idsToPositions.put(three.getId(), 3);
    //
    // List<Player> players =
    // new ArrayList<>(ImmutableList.of(zero, one, two, three));
    // Collections.shuffle(players);
    //
    // Game g = new Game(players, get10cards());
    // g.play();
    // for (int w : g.winners()) {
    // numWon[idsToPositions.get(w)] = numWon[idsToPositions.get(w)] + 1;
    // }
    //
    // if ((int) (i * 100.0 / sampleSize) > (int) ((i - 1) * 100.0
    // / sampleSize)) {
    // System.out.println((int) (i * 100.0 / sampleSize) + "%");
    // }
    // }
    // System.out.println(Arrays.toString(numWon));

    // double sampleSize = 50000.0;
    // int[] numWon = new int[] {0, 0, 0};
    // Map<Integer, Integer> idsToPositions = new HashMap<>();
    // for (int i = 0; i < sampleSize; i++) {
    // Player zero = new AIPlayer(new NeuralNetMine(0)),
    // one = new AIPlayer(new NeuralNetMine(1)),
    // two = new AIPlayer(new NeuralNetMine(2));
    // idsToPositions.put(zero.getId(), 0);
    // idsToPositions.put(one.getId(), 1);
    // idsToPositions.put(two.getId(), 2);
    //
    // List<Player> players = new ArrayList<>(ImmutableList.of(zero, one, two));
    // Collections.shuffle(players);
    //
    // Game g = new Game(players, get10cards());
    // g.play();
    // for (int w : g.winners()) {
    // numWon[idsToPositions.get(w)] = numWon[idsToPositions.get(w)] + 1;
    // }
    //
    // if ((int) (i * 100.0 / sampleSize) > (int) ((i - 1) * 100.0
    // / sampleSize)) {
    // System.out.println((int) (i * 100.0 / sampleSize) + "%");
    // }
    // }
    // System.out.println(Arrays.toString(numWon));

    double sampleSize = 50000.0;
    int[] numWon = new int[] {0, 0};
    Map<Integer, Integer> idsToPositions = new HashMap<>();
    for (int i = 0; i < sampleSize; i++) {
      Player zero = new AIPlayer(new NeuralNetMine(0)),
          one = new AIPlayer(new NeuralNetMine(1));
      idsToPositions.put(zero.getId(), 0);
      idsToPositions.put(one.getId(), 1);

      List<Player> players = new ArrayList<>(ImmutableList.of(zero, one));
      Collections.shuffle(players);

      Game g = new Game(players, get10cards());
      g.play();
      for (int w : g.winners()) {
        numWon[idsToPositions.get(w)] = numWon[idsToPositions.get(w)] + 1;
      }

      if ((int) (i * 100.0 / sampleSize) > (int) ((i - 1) * 100.0
          / sampleSize)) {
        System.out.println((int) (i * 100.0 / sampleSize) + "%");
      }
    }
    System.out.println(Arrays.toString(numWon));

    // NeuralNetwork nn = NeuralNetworkIO.load("src/main/resources/best.nn");
    // System.out
    // .println(Arrays.toString(nn.run(new double[] {0.7, 0, 0, 0.3, 0, 0})));

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
