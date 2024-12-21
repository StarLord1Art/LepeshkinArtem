package dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ArticleCreateCommentRequest(String articleId, String text) {
  public ArticleCreateCommentRequest(
          @JsonProperty("articleId") String articleId,
          @JsonProperty("text") String text
  ) {
    this.articleId = articleId;
    this.text = text;
  }
}