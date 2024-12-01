package java.main.org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersList implements UserRepository {

  protected static List<User> usersList = Collections.synchronizedList(new ArrayList<>());

  @Override
  public User findByMsisdn(String msisdn) {
    synchronized (usersList) {
      for (User user : usersList) {
        if (user.info.containsValue(msisdn)) {
          return user;
        }
      }
      return null;
    }
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    synchronized (usersList) {
      User searchResult = findByMsisdn(msisdn);
      if (searchResult != null) {
        searchResult = user;
      } else {
        throw new NoSuchUser("No user with such msisdn.");
      }
    }
  }

  public void addUser(User user) {
    if (user == null) {
      throw new NullParameterException("Null can`t be added to list.");
    }
    usersList.add(user);
  }
}
