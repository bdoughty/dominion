package edu.brown.cs.dominion;

import static spark.Spark.exception;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.webSocket;

import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.dominion.games.GameManager;
import edu.brown.cs.dominion.io.HomeWebsocket;
import edu.brown.cs.dominion.io.UserRegistry;
import edu.brown.cs.dominion.io.Websocket;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Created by henry on 3/22/2017.
 */
public class Main {
  private static final int DEFAULT_PORT = 4567;
  private UserRegistry users;
  private HomeWebsocket home;

  public static void main(String[] args) {
    Main m = new Main();
    m.run(args);
  }

  private Main() {
    users = new UserRegistry();
    GameManager gm = new GameManager(users);
    home = new HomeWebsocket(gm);
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

    webSocket("/home", new Websocket(users, home));

    // TODO get rid of this, for some reason it is necessary for the server to
    // start right now.
    get("/hello", (req, res) -> "Hello World");
  }

  /**
   * Display an error page when an exception occurs in the server. From STARS
   * git.
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
