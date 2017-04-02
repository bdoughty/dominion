package edu.brown.cs.dominion;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ChatWebSocketHandler {
  public ChatWebSocketHandler(String localData){
    this.localData = localData;
  }
  private String localData;

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    System.out.println(localData + "New User Connected");
    user.getRemote().sendString(localData);
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    System.out.println(localData + "User Left");
  }

  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    System.out.println(localData + "Received New Message From User");
  }
}