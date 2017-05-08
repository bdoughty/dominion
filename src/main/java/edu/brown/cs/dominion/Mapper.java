package edu.brown.cs.dominion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by henry on 5/7/2017.
 */
public class Mapper {
  public static <T, K> List<K> map(List<T> l, Function<T, K> fun) {
    List<K> n = new ArrayList<K>();
    l.forEach(e -> n.add(fun.apply(e)));
    return n;
  }
}
