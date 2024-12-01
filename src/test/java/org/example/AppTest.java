package test.java.org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.main.org.example.EnrichByMsisdn;
import java.main.org.example.EnrichmentService;
import java.main.org.example.Message;
import java.main.org.example.User;
import java.main.org.example.UsersList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

public class AppTest {

  @Test
  void shouldReturnEnrichedMessage() {
    UsersList usersList = new UsersList();
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn()));
    Message message =
        new Message(
            new HashMap(Map.of("page", "book_card", "msisdn", "89992222307")),
            Message.EnrichmentType.MSISDN);
    Message enrichedMessage =
        new Message(
            new HashMap(
                Map.of(
                    "firstName",
                    "Ivan",
                    "lastName",
                    "Ivanov",
                    "msisdn",
                    "89992222307",
                    "page",
                    "book_card")),
            Message.EnrichmentType.MSISDN);
    usersList.addUser(new User(message.content));
    enrichmentService.enrich(message);
    usersList.updateUserByMsisdn(message.content.get("msisdn"), new User(message.content));

    assertEquals(enrichedMessage, message);
  }

  @Test
  void shouldSucceedEnrichmentInConcurrentEnvironmentSuccessfully() throws InterruptedException {
    Message messageForComparison =
        new Message(
            new HashMap(
                Map.of("firstName", "Ivan", "lastName", "Ivanov", "msisdn", "89162345678")),
            Message.EnrichmentType.MSISDN);
    Message m1 =
        new Message(
            new HashMap(
                Map.of("firstName", "Sasha", "lastName", "Kozlov", "msisdn", "89162345678")),
            Message.EnrichmentType.MSISDN);

    UsersList usersList = new UsersList();
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn()));
    List<Message> enrichmentResults = new CopyOnWriteArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    CountDownLatch latch = new CountDownLatch(5);

    for (int i = 0; i < 5; i++) {
      executorService.submit(
          () -> {
            usersList.addUser(new User(m1.content));
            enrichmentResults.add(enrichmentService.enrich(m1));
            usersList.updateUserByMsisdn(m1.content.get("msisdn"), new User(m1.content));
            latch.countDown();
          });
    }
    latch.await();

    for (Message message : enrichmentResults) {
      assertEquals(message, messageForComparison);
    }
  }
}
