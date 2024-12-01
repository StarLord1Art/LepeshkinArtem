package java.main.org.example;

public class EnrichByMsisdn implements Enriching {

  @Override
  public Message.EnrichmentType type() {
    return Message.EnrichmentType.MSISDN;
  }

  @Override
  public Message enrich(Message message) {
    if (message.content.containsKey("msisdn")) {
      message.content.put("firstName", "Ivan");
      message.content.put("lastName", "Ivanov");
    }
    return message;
  }
}
