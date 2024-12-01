package org.example.service;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.entity.Comment;

import org.example.repository.ArticleRepository;
import org.example.repository.exception.ArticleIdDuplicatedException;
import org.example.repository.exception.ArticleNotFoundException;

import org.example.service.exception.ArticleCreateException;
import org.example.service.exception.ArticleDeleteException;
import org.example.service.exception.ArticleFindException;
import org.example.service.exception.ArticleUpdateException;

import java.util.List;
import java.util.Set;

public class ArticleService {
  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(ArticleId articleId) {
    try {
      return articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article by id=" + articleId, e);
    }
  }

  public ArticleId create(String name, Set<String> tags) {
    ArticleId articleId = articleRepository.generateId();
    Article article = new Article(articleId, name, tags, null);
    try {
      articleRepository.create(article);
    } catch (ArticleIdDuplicatedException e) {
      throw new ArticleCreateException("Cannot create article", e);
    }
    return articleId;
  }

  public void update(ArticleId articleId, String name, Set<String> tags, List<Comment> comments) {
    Article article;
    try {
      article = articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot find article with id=" + articleId, e);
    }

    try {
      articleRepository.update(article.withName(name).withTags(tags).withComments(comments));
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update article with id=" + articleId, e);
    }
  }

  public void delete(ArticleId articleId) {
    try {
      articleRepository.delete(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id=" + articleId, e);
    }
  }
}
