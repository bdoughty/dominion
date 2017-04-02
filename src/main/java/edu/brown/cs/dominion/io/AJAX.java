package edu.brown.cs.dominion.io;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * an ajax annotation to allow for easy reflection.
 * Created by henry on 2/16/2017.
 */

@Retention(RetentionPolicy.RUNTIME)

public @interface AJAX {
  /**
   * @return the error statements to failing to conform to the corresponding
   * specification.
   */
  String[] names() default {};
}
