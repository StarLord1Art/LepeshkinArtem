package java.main.org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    UsersList usersList = new UsersList();
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn()));
    Message message =
        new Message(
            new HashMap(
                Map.of(
                    "action",
                    "button_click",
                    "page",
                    "book_card",
                    "msisdn",
                    "88005553535",
                    "firstName",
                    "Artem")),
            Message.EnrichmentType.MSISDN);

    usersList.addUser(new User(message.content));
    System.out.println(UsersList.usersList.get(0).info);

    enrichmentService.enrich(message);
    System.out.println(message.content);

    usersList.updateUserByMsisdn(
        message.content.get("msisdn"), usersList.findByMsisdn(message.content.get("msisdn")));
    System.out.println(UsersList.usersList.get(0).info);

    Message mess = new Message(null, null);
    usersList.addUser(new User(mess.content));
  }
}
