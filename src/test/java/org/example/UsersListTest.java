package test.java.org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.main.org.example.NoSuchUser;
import java.main.org.example.User;
import java.main.org.example.UsersList;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UsersListTest {

  @Test
  void testFindByMsisdnShouldReturnNull() {
    UsersList usersList = new UsersList();
    usersList.addUser(new User(new HashMap(Map.of("Name", "Misha"))));
    User res = usersList.findByMsisdn("89173546756");

    assertEquals(res, null);
  }

  @Test
  void testShouldFindUser() {
    UsersList usersList = new UsersList();
    User userForComparison = new User(Map.of("Name", "Misha", "msisdn", "8971234567"));
    usersList.addUser(new User(new HashMap(Map.of("Name", "Misha", "msisdn", "8971234567"))));
    User user = usersList.findByMsisdn("8971234567");

    assertEquals(user, userForComparison);
  }

  @Test
  void testUpdateShouldThrowNoSuchUserException() {
    UsersList usersList = new UsersList();
    assertThrows(
        NoSuchUser.class,
        () -> usersList.updateUserByMsisdn("1223", new User(Map.of("name", "Artem"))));
  }
}
