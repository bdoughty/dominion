package edu.brown.cs.dominion.io.send;

import com.google.gson.JsonObject;
import edu.brown.cs.dominion.io.ButtonCallback;

/**
 * Created by henry on 4/29/2017.
 */
public class ButtonCall{
  private String name;
  private int id;
  private transient ButtonCallback bc;

  public ButtonCall(String name, ButtonCallback bc) {
    this.name = name;
    this.id = ClientUpdateMap.nextButtonId++;
    this.bc = bc;
  }

  public int getId() {
    return id;
  }

  public ButtonCallback getBc() {
    return bc;
  }

  public String getName() {
    return name;
  }
}
