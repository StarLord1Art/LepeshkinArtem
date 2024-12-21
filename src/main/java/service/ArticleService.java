package service;

import Exeptions.*;
import entity.Article;
import entity.ArticleId;
import entity.CommentId;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import repositories.ArticleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArticleService {

  private final ArticleRepository articleRepository;
  private final Jdbi jdbi;

  public ArticleService(ArticleRepository articleRepository, Jdbi jdbi) {
    this.articleRepository = articleRepository;
    this.jdbi = jdbi;
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(ArticleId articleId) throws ArticleFindException {
    try {
      return articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article by id=" + articleId.id());
    }
  }

  public long create(String name) throws ArticleCreateException {
    return articleRepository.create(name);
  }

  public List<Long> createArticles(Set<String> names) {
    return jdbi.inTransaction((Handle handle) -> {
      List<Long> result = new ArrayList<>();
      for (String name : names) {
          long id = articleRepository.create(name);
          result.add(id);
      }
      return result;
    });
  }

  public void update(ArticleId articleId, String name) throws ArticleUpdateException {
    try {
      articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot find article with id=" + articleId.id());
    }

    try {
      articleRepository.update(name, articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update article with id=" + articleId.id());
    }
  }

  public void delete(ArticleId articleId) throws ArticleDeleteException {
    try {
      articleRepository.delete(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id=" + articleId);
    }
  }

  public long createComment(ArticleId articleId, String text) throws ArticleFindException {
    try {
      findById(articleId);
    } catch (ArticleFindException e) {
      throw new ArticleFindException("Cannot find article with id " + articleId.id());
    }

    return articleRepository.addComment(articleId, text);
  }

  public void deleteComment(ArticleId articleId, CommentId commentId) throws ArticleFindException {
    try {
      findById(articleId);
    } catch (ArticleFindException e) {
      throw new ArticleFindException("Cannot find article with id " + articleId.id());
    }

    articleRepository.deleteCommentFromArticle(articleId, commentId);
  }
}
