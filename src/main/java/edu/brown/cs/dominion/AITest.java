package edu.brown.cs.dominion;

import com.google.common.collect.ImmutableList;
import edu.brown.cs.dominion.AI.AIPlayer;
import edu.brown.cs.dominion.AI.Strategy.DumbStrategy;
import edu.brown.cs.dominion.games.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Use random play ai to test game functionality.
 * Created by henry on 5/7/2017.
 */
public class AITest {
  public static void main(String[] args) {
    for(int i = 0;i < 100;i++){
      Game g = new Game(ImmutableList.of(
        new AIPlayer(new DumbStrategy())),
        get10cards());
      g.play();
    }
  }

  private static List<Integer> get10cards(){
    List<Integer> a = new ArrayList<>();
    while(a.size() < 10){
      int n = (int) (Math.random() * 22 + 7);
      if(!a.contains(n)){
        a.add(n);
      }
    }
    return a;
  }
}
