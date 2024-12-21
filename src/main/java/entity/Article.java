package entity;

import java.util.*;

public class Article {

  private final ArticleId id;
  private final String name;
  private final List<Comment> comments;
  private boolean trending;

  public Article(ArticleId id, String name) {
    this.id = id;
    this.name = name;
    this.comments = new ArrayList<>();
    trending = false;
  }

  public Article withName(String newName) {
    return new Article(id, newName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Article book = (Article) o;
    return id == book.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public void deleteComment(CommentId commentId) {
    for (Comment currentComment: comments) {
      if (currentComment.getId().id() == commentId.id()) {
        comments.remove(currentComment);
        break;
      }
    }
  }

  public long getCountOfComments() {
    return comments.size();
  }

  public boolean isTrending() {
    return trending;
  }

  public Article setTrending(boolean trending) {
    this.trending = trending;
    return this;
  }
}