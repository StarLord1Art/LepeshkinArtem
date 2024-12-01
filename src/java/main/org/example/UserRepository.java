package java.main.org.example;

public interface UserRepository {

  User findByMsisdn(String msisdn);

  void updateUserByMsisdn(String msisdn, User user);
}
