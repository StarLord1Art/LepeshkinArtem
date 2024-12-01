package org.example.repository;

import org.example.entity.ArticleId;
import org.example.entity.Article;

import java.util.List;

public interface ArticleRepository {
  ArticleId generateId();

  List<Article> findAll();

  Article findById(ArticleId articleId);

  void create(Article article);

  void update(Article article);

  void delete(ArticleId articleId);
}
