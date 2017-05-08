package edu.brown.cs.dominion.io.send;

import edu.brown.cs.dominion.players.UserInteruptedException;

/**
 * A button to be displayed on the client side in the button selection pane.
 * Created by henry on 5/5/2017.
 */
public class Button implements ButtonCallback{
  private static int nextId = 0;

  // the unique id of the button
  private int id;
  // the name of the button
  private String name;
  // what to do when the button is pressed
  private transient ButtonCallback bc;

  /**
   * Basic constructor for a button
   * @param name the buttons name
   * @param bc what to do when the button is pressed
   */
  public Button(String name, ButtonCallback bc) {
    this.bc = bc;
    this.id = nextId++;
    this.name = name;
  }

  /**
   * get the id of the button.
   * @return the id of the button.
   */
  public int getId() {
    return id;
  }

  /**
   * get the name of the button.
   * @return the name of the button.
   */
  public String getName() {
    return name;
  }

  @Override
  public void pressed() throws UserInteruptedException{
    bc.pressed();
  }
}
