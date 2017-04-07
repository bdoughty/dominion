package edu.brown.cs.dominion.io;

import com.google.gson.Gson;
import edu.brown.cs.dominion.User;
import edu.brown.cs.dominion.io.send.Sender;

import java.util.Collection;
import java.util.Iterator;

public class UserSet implements Collection<User>, Sender {
  private static final Gson GSON = new Gson();
  private Collection<User> users;

  @Override
  public boolean send(String s) {
    users.forEach(user -> user.send(s));
    return true;
  }

  /*
          Decorator and Constructor (Wrapper) Methods Below
   */

  public UserSet(Collection<User> users) {
    this.users = users;
  }

  @Override
  public int size() {
    return users.size();
  }

  @Override
  public boolean isEmpty() {
    return users.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return users.contains(o);
  }

  @Override
  public Iterator<User> iterator() {
    return users.iterator();
  }

  @Override
  public Object[] toArray() {
    return users.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return users.toArray(a);
  }

  @Override
  public boolean add(User user) {
    return users.add(user);
  }

  @Override
  public boolean remove(Object o) {
    return users.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return users.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends User> c) {
    return users.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return users.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return users.retainAll(c);
  }

  @Override
  public void clear() {
    users.clear();
  }
}
