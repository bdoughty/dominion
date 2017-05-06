package edu.brown.cs.dominion.io.send;

/**
 * Created by henry on 5/5/2017.
 */
public class Button implements ButtonCallback{
  private static int nextId = 0;

  private int id;
  private String name;
  private transient ButtonCallback bc;

  public Button(String name, ButtonCallback bc) {
    this.bc = bc;
    this.id = nextId++;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public void pressed() {
    bc.pressed();
  }
}
