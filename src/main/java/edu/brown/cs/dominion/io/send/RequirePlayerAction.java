package edu.brown.cs.dominion.io.send;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.ButtonCallback;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by henry on 5/4/2017.
 */
public class RequirePlayerAction implements SelectCallback{
  private static Gson GSON = new Gson();
  private List<ButtonCall> bcs = null;
  private Callback c = null;
  private boolean urgent;

  public RequirePlayerAction (List<ButtonCall> bcs, boolean urgent) {
    this.bcs = bcs;
    this.urgent = urgent;
  }

  public RequirePlayerAction (Callback c, boolean urgent) {
    this.c = c;
    this.urgent = urgent;
  }

  public RequirePlayerAction (List<ButtonCall> bcs, Callback c, boolean
    urgent) {
    this.bcs = bcs;
    this.c = c;
    this.urgent = urgent;
  }

  public JsonObject toJson(){
    JsonObject main = new JsonObject();
    main.addProperty("urgent", this.isUrgent());
    main.addProperty("select", c != null);
    main.add("handselect",
      GSON.toJsonTree(c != null ? c.getHandIds() : new ArrayList<>()));
    main.add("boardselect",
      GSON.toJsonTree(c != null ? c.getBoardIds() : new ArrayList<>()));
    main.add("buttons", GSON.toJsonTree(bcs));
    return main;
  }

  public boolean isUrgent() {
    return urgent;
  }

  public Callback getC() {
    return c;
  }

  public List<ButtonCall> getBcs() {
    return bcs;
  }

  @Override
  public ClientUpdateMap call(User u, boolean inHand, int loc) {
    return c.getCallback().call(u, inHand, loc);
  }

  public static RequirePlayerAction callback(List<Integer> handIds,
                                             List<Integer> boardIds,
                                             SelectCallback sc, String name){
    Callback c = new Callback(handIds, boardIds, sc, name);
    return new RequirePlayerAction(c, true);
  }

  public static RequirePlayerAction callback(List<Integer> handIds,
                                             List<Integer> boardIds,
                                             SelectCallback sc, CancelHandler
                                               ch, String name){
    Callback c = new Callback(handIds, boardIds, sc, ch, name);
    return new RequirePlayerAction(c, true);
  }

  public static RequirePlayerAction buttons(ButtonCall... bc){
    return new RequirePlayerAction(Arrays.asList(bc), true);
  }



}
