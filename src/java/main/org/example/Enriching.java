package java.main.org.example;

public interface Enriching {

  Message.EnrichmentType type();

  Message enrich(Message message);
}
