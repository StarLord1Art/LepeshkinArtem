package org.example.entity;

import java.util.List;
import java.util.Set;

public record Article(ArticleId id, String name, Set<String> tags, List<Comment> comments) {

  public Article withName(String newName) {
    return new Article(id, newName, tags, comments);
  }

  public Article withTags(Set<String> newTags) {
    return new Article(id, name, newTags, comments);
  }

  public Article withComments(List<Comment> newComments) {
    return new Article(id, name, tags, newComments);
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getTags() {
    return tags.toString();
  }

  public List<Comment> getComments() {
    return comments;
  }
}
