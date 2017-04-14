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
  private static Gson GSON;

  Collection<Session> users;

  public Chat(Collection<Session> sessions) {
    this.users = sessions;
    GSON = new Gson();
  }

  public void sendMessage(String name, String color, String message){
    JsonObject container = new JsonObject();
    container.addProperty("name", name);
    container.addProperty("color", color);
    container.addProperty("message", message);
    String toSend = GSON.toJson(container);

    for (Session s : users) {
      try {
        s.getRemote().sendString(toSend);
      } catch (IOException e) { e.printStackTrace(); /* TODO */ }
    }
  }
}
