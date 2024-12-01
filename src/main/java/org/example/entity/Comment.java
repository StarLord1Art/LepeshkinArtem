package org.example.entity;

public record Comment(CommentId id, ArticleId articleId, String text) {

  public CommentId getId() {
    return id;
  }

  public ArticleId getArticleId() {
    return articleId;
  }

  public String getText() {
    return text;
  }
}
