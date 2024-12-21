package dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ArticleCreateRequest(String name) {
  public ArticleCreateRequest(
          @JsonProperty("name") String name
  ) {
    this.name = name;
  }
}