package edu.brown.cs.dominion.io;

import edu.brown.cs.dominion.Card;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * A wrapper that adds various ajax functionality to the server and then
 * cleans the data for use by the GameEvenListener.
 *
 * Created by henry on 3/22/2017.
 */
public class GameEventWrapper {
  private UserRegistry users;

  public GameEventWrapper(GameEventListener gel, UserRegistry users) {
    this.users = users;
    this.gel = gel;
  }

  private GameEventListener gel;
  private Function<Card, ClientUpdateMap> callback;

  @AJAX(names = {"userId", "boughtCards"})
  public ClientUpdateMap buys(Integer userId, List<Integer> buys){
    List<Card> cards = map(buys, Card::getCardFromCardId);
    return chk(gel.endBuyPhase(users.getUserById(userId), cards));
  }

  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap action(Integer userId, Integer cardId){
    return chk(gel.doAction(users.getUserById(userId),
        Card.getCardFromCardId(cardId)));
  }

  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap action(Integer userId){
    return chk(gel.endActionPhase(users.getUserById(userId)));
  }

  @AJAX(names = {"userId", "cardId"})
  public ClientUpdateMap selection(Integer userId, Integer cardId){
    assert callback != null;

    return chk(callback.apply(Card.getCardFromCardId(cardId))).finishSelect();
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
