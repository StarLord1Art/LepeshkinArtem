package test.java.org.example;

import java.main.org.example.EnrichByMsisdn;
import java.main.org.example.Message;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrichByMsisdnTest {

  @Test
  void testShouldEnrichMessage() {
    EnrichByMsisdn enriching = new EnrichByMsisdn();
    Message message = new Message(new HashMap(Map.of("msisdn", "1234567890")),
        Message.EnrichmentType.MSISDN);
    Message messageForComparison = new Message(
        new HashMap(Map.of("firstName", "Ivan", "lastName", "Ivanov", "msisdn", "1234567890")),
        Message.EnrichmentType.MSISDN);
    enriching.enrich(message);

    assertEquals(message, messageForComparison);
  }

  @Test
  void testShouldReturnUntouchedMessage() {
    EnrichByMsisdn enriching = new EnrichByMsisdn();
    Message message = new Message(new HashMap(Map.of("firstName", "Ivan")),
        Message.EnrichmentType.MSISDN);
    Message messageForComparison = new Message(new HashMap(Map.of("firstName", "Ivan")),
        Message.EnrichmentType.MSISDN);
    enriching.enrich(message);

    assertEquals(message, messageForComparison);
  }
}
