package edu.brown.cs.dominion.io;

/**
 * Created by henry on 3/23/2017.
 */

import java.util.function.Function;

/**
 * A simple callback using lists.
 *

 */
@FunctionalInterface
public interface IOThrowFunction<F, T> extends Function<F, T> {
  @Override
  default T apply(F input) {
    try {
      return applyThrows(input);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param input
   *          The input to the function
   * @return The output of the function.
   * @throws Exception
   *           If the interior apply throws an exception.
   */
  T applyThrows(F input) throws Exception;
}