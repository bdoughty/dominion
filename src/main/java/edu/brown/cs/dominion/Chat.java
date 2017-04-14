package edu.brown.cs.dominion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jdk.nashorn.api.scripting.JSObject;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A Chat Object
 * Created by henry on 4/2/2017.
 */
public class Chat {
  private static Gson GSON = new Gson();

  public Chat() {}

  public String getMessage(String name, String color, String message){
    JsonObject container = new JsonObject();
    container.addProperty("name", name);
    container.addProperty("color", color);
    container.addProperty("message", message);
    String toSend = GSON.toJson(container);
    return toSend;
  }
}
