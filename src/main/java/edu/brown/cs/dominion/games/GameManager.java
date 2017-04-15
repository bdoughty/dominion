package edu.brown.cs.dominion.games;

import edu.brown.cs.dominion.Card;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.AJAX;
import edu.brown.cs.dominion.io.UserRegistry;
import edu.brown.cs.dominion.io.send.ClientUpdateMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A wrapper that adds various ajax functionality to the server and then
 * cleans the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameManager {
  private UserRegistry users;
  private Map<User, Game> gamesByUser;
  private List<Game> games;

  public GameManager(UserRegistry users) {
    this.users = users;
    gamesByUser = new HashMap<>();
    games = new LinkedList<>();
  }

  private Function<Card, ClientUpdateMap> callback;

  @AJAX(names = {"userId", "boughtCards"})
  public ClientUpdateMap buys(Integer userId, List<Integer> buys){
    List<Card> cards = map(buys, Card::getCardFromId);
    User user = users.getById(userId);
    Game g = gamesByUser.get(user);
    return chk(g.endBuyPhase(user, cards));
  }

  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap action(Integer userId, Integer cardId){
    User user = users.getById(userId);
    Game g = gamesByUser.get(user);
    return chk(g.doAction(user, Card.getCardFromId(cardId)));
  }

  //TODO WHAT IS THIS!!??!?!?!?!?
  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap endActionPhase(Integer userId){
    User user = users.getById(userId);
    Game g = gamesByUser.get(user);
    return chk(g.endActionPhase(user));
  }

  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap selection(Integer userId, Integer cardId){
    assert callback != null;

    return chk(callback.apply(Card.getCardFromId(cardId))).finishSelect();
  }

  private <T, K> List<K> map(List<T> list, Function<T, K> convert) {
    List<K> output = new LinkedList<>();
    list.forEach(s -> output.add(convert.apply(s)));
    return output;
  }

  private ClientUpdateMap chk(ClientUpdateMap c) {
    if(c.hasCallback()) {
      callback = c.getCallback();
    }
    return c;
  }
}
