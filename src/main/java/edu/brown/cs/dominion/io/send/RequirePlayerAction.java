package edu.brown.cs.dominion.io.send;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.dominion.players.PlayerWake;
import edu.brown.cs.dominion.players.UserPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 5/4/2017.
 */
public class RequirePlayerAction {
  private static Gson GSON = new Gson();


  private List<Button> buttons = null;
  private Callback c = null;
  private transient UserPlayer player;
  private transient PlayerWake waker;
  private boolean urgent;

  public RequirePlayerAction (UserPlayer p, PlayerWake waker, List<Button>
    buttons, boolean urgent) {
    this.player = p;
    this.buttons = buttons;
    this.urgent = urgent;
    this.waker = waker;
  }

  public RequirePlayerAction (UserPlayer p, PlayerWake waker, Callback c, boolean urgent) {
    this.player = p;
    this.c = c;
    this.urgent = urgent;
    this.waker = waker;
  }

  public RequirePlayerAction (UserPlayer p, PlayerWake waker, List<Button> buttons, Callback c,
                              boolean urgent) {
    this.player = p;
    this.buttons = buttons;
    this.c = c;
    this.urgent = urgent;
    this.waker = waker;
  }

  public JsonObject toJson(){
    JsonObject main = new JsonObject();
    main.addProperty("urgent", this.isUrgent());
    main.addProperty("select", c != null);
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

  public void call(int response) {
    player.wakeType = waker;
    player.wakeData = response;
    player.notifyAll();
  }
}
