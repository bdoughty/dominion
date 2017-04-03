package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;
import edu.brown.cs.dominion.io.send.Jsonable;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Method;
import java.util.List;
import static spark.Spark.*;

/**
 * Created by henry on 3/22/2017.
 */
public class SparkManager {
  private static final Gson GSON = new Gson();

  public ClientUpdateMap buy(List<Integer> numbers) {
    return new ClientUpdateMap();
  }

  public void mount(Object o){
    Method[] methods = o.getClass().getDeclaredMethods();

    for (Method m : methods) {
      //check for ajax annotation
      if (m.getAnnotation(AJAX.class) != null
          && m.getReturnType() == ClientUpdateMap.class) {
        AJAX a = m.getAnnotation(AJAX.class);

        post("/" + m.getName(), new SparkReflectionHandler(m, o, a));
      }
    }
  }

  private class SparkReflectionHandler implements Route {
    Method m;
    Object callOn;
    String[] readNames;
    public SparkReflectionHandler(Method m, Object callOn, AJAX a) {
      this.m = m;
      this.callOn = callOn;
      this.readNames = a.names();
    }
    @Override
    public String handle(Request request, Response response) throws Exception {
      QueryParamsMap query = request.queryMap();
      Object[] params = new Object[readNames.length];

      for (int i = 0; i < readNames.length; i++) {
        String read = readNames[i];
        if (query.hasKey(read)) {
          params[i] = query.get(read);
        } else {
          throw new Exception("Post request lacked " + read + " variable in " +
              "the map.");
        }
      }

      Object o = m.invoke(callOn, params);
      if (o instanceof Jsonable){
        return ((Jsonable) o).prepare();
      } else {
        throw new Exception("Output for post is not Jsonable.");
      }
    }
  }
}
