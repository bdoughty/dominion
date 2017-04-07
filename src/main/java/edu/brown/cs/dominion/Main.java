package edu.brown.cs.dominion;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import edu.brown.cs.dominion.io.UserRegistry;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by henry on 3/22/2017.
 */
public class Main {
  private static final int DEFAULT_PORT = 4567;
  private UserRegistry users;

  public static void main(String[] args) {
    Main m = new Main();
    m.run(args);
    Gson g = new Gson();
    System.out.println(g.toJson(new User(4)));
  }

  private Main() {
    users = new UserRegistry();
  }

  private void run(String[] args) {
    OptionParser parser = new OptionParser();
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    runSparkServer((int) options.valueOf("port"));
  }

  private void runSparkServer(int portNum) {
    port(portNum);
    externalStaticFileLocation("resources");
    exception(Exception.class, new ExceptionPrinter());

    webSocket("/socket", users);

    //TODO get rid of this, for some reason it is necessary for the server to
    // start right now.
    get("/hello", (req, res) -> "Hello World");
  }


  /**
   * Display an error page when an exception occurs in the server.
   * From STARS git.
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}














