package edu.brown.cs.dominion.user;

import edu.brown.cs.dominion.Main;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.Map;

/**
 * A wrapper for session that allows users to contain extra information
 * associated with the user concept.
 *
 * Created by henry on 3/22/2017.
 */
public class User implements Session {
  private int id;
  private Color c;
  private String name;
  private Session session;

  public User(Session s, int id) {
    this.session = s;
    c = makeColor();
    this.id = id;
  }

  /**
   * Getter for the name of the user, will return a string conversion of the
   * users ID.
   * @return the name of the user.
   */
  public String getName() {
    if (name != null) {
      return name;
    } else {
      return Integer.toString(id);
    }
  }

  public int getId() {
    return id;
  }

  public String getColor() {
    return "rgb("+ c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
  }

  private Color makeColor(){
    return Color.getHSBColor((float) Math.random(), 0.5f, 0.5f);
  }







  /*
            DECORATOR METHODS FOR SESSION FOUND BELOW.
   */

  public void close() {
    session.close();
  }

  public void close(CloseStatus closeStatus) {
    session.close(closeStatus);
  }

  public void close(int i, String s) {
    session.close(i, s);
  }

  public void disconnect() throws IOException {
    session.disconnect();
  }

  public long getIdleTimeout() {
    return session.getIdleTimeout();
  }

  public InetSocketAddress getLocalAddress() {
    return session.getLocalAddress();
  }

  public WebSocketPolicy getPolicy() {
    return session.getPolicy();
  }

  public String getProtocolVersion() {
    return session.getProtocolVersion();
  }

  public RemoteEndpoint getRemote() {
    return session.getRemote();
  }

  public InetSocketAddress getRemoteAddress() {
    return session.getRemoteAddress();
  }

  public UpgradeRequest getUpgradeRequest() {
    return session.getUpgradeRequest();
  }

  public UpgradeResponse getUpgradeResponse() {
    return session.getUpgradeResponse();
  }

  public boolean isOpen() {
    return session.isOpen();
  }

  public boolean isSecure() {
    return session.isSecure();
  }

  public void setIdleTimeout(long l) {
    session.setIdleTimeout(l);
  }

  public SuspendToken suspend() {
    return session.suspend();
  }
}
