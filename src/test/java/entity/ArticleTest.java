package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

  @Test
  void getId() {
    Article article = new Article(new ArticleId(0), "qwerty");
    long response = article.getId().id();
    assertEquals(response, 0);
  }

  @Test
  void getName() {
    Article article = new Article(new ArticleId(0), "qwerty");
    String response = article.getName();
    assertEquals(response, "qwerty");
  }

  @Test
  void addComment() {
    Article article = new Article(new ArticleId(0), "qwerty");
    article.addComment(new Comment(new CommentId(0), new ArticleId(0), "123"));
    long response = article.getCountOfComments();
    assertEquals(response, 1);
  }

  @Test
  void deleteComment() {
    Article article = new Article(new ArticleId(0), "qwerty");
    article.addComment(new Comment(new CommentId(0), new ArticleId(0), "123"));
    article.deleteComment(new CommentId(0));
    long response = article.getCountOfComments();
    assertEquals(response, 0);
  }

  @Test
  void getCountOfComments() {
    Article article = new Article(new ArticleId(0), "qwerty");
    article.addComment(new Comment(new CommentId(0), new ArticleId(0), "123"));
    long response = article.getCountOfComments();
    assertEquals(response, 1);
  }
}