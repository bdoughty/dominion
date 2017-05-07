package edu.brown.cs.dominion.io.send;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.dominion.players.PlayerWake;
import edu.brown.cs.dominion.players.UserPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * An objects that represents an action that the playyer has to do
 * Created by henry on 5/4/2017.
 */
public class RequirePlayerAction {
  public static int nextId = 0;
  private static Gson GSON = new Gson();


  private List<Button> buttons = null;
  private Callback c = null;
  private boolean urgent;
  private int id;


  public RequirePlayerAction (List<Button> buttons, boolean urgent, int id) {
    this.buttons = buttons;
    this.urgent = urgent;
    this.id = id;
  }

  public RequirePlayerAction (Callback c, boolean urgent, int id) {
    this.c = c;
    this.urgent = urgent;
    this.id = id;
  }

  public RequirePlayerAction (List<Button> buttons, Callback c,
                              boolean urgent, int id) {
    this.buttons = buttons;
    this.c = c;
    this.urgent = urgent;
    this.id = id;
  }

  public JsonObject toJson(){
    JsonObject main = new JsonObject();
    main.addProperty("urgent", this.isUrgent());
    main.addProperty("select", c != null);
    main.addProperty("cancel", c != null && c.isStoppable());
    main.addProperty("id", id);
    main.add("handselect",
      GSON.toJsonTree(c != null ? c.getHandIds() : new ArrayList<>()));
    main.add("boardselect",
      GSON.toJsonTree(c != null ? c.getBoardIds() : new ArrayList<>()));
    main.add("buttons", GSON.toJsonTree(buttons));
    return main;
  }

  public boolean isUrgent() {
    return urgent;
  }

  public Callback getC() {
    return c;
  }

  public List<Button> getBcs() {
    return buttons;
  }

  public int getId() {
    return id;
  }
}
