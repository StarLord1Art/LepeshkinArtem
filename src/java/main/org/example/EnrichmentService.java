package java.main.org.example;

import java.util.List;

public class EnrichmentService {

  private final List<Enriching> enrichments;

  public EnrichmentService(List<Enriching> enrichments) {
    this.enrichments = enrichments;
  }

  public synchronized Message enrich(Message message) {
    if (message == null) {
      throw new NullParameterException("Null can`t be enriched.");
    }
    for (Enriching enrichment : this.enrichments) {
      if (enrichment.type().equals(message.enrichmentType)) {
        enrichment.enrich(message);
        return message;
      }
    }
    return message;
  }
}
