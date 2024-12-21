package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public record SomeArticlesCreateRequest(Set<String> names) {
  public SomeArticlesCreateRequest(
          @JsonProperty("names") Set<String> names
  ) {
    this.names = names;
  }
}
