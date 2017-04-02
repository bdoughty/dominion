package edu.brown.cs.dominion;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Display an error page when an exception occurs in the server.
 * From STARS git.
 * @author jj
 */
public class ExceptionPrinter implements ExceptionHandler {
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