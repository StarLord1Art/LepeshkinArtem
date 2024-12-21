package dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ArticlePutRequest(String articleId, String name) {
  public ArticlePutRequest(
          @JsonProperty("articleId") String articleId,
          @JsonProperty("name") String name
  ) {
    this.articleId = articleId;
    this.name = name;
  }
}