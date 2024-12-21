package repositories;

import Exeptions.ArticleDeleteException;
import Exeptions.ArticleIdDuplicatedException;
import Exeptions.ArticleNotFoundException;
import entity.Article;
import entity.ArticleId;
import entity.Comment;
import entity.CommentId;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleRepository implements ArticleRepositoryInterface {

  private final Jdbi jdbi;

  public ArticleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public List<Article> findAll() {
    return jdbi.inTransaction((Handle handle) -> {
      List<Map<String, Object>> result = handle.createQuery("SELECT id, name, trending FROM article")
          .mapToMap()
          .list();
      return result.stream().map(element -> new Article(
          (new ArticleId((Long) element.get("id"))),
          ((String) element.get("name"))).setTrending((boolean) element.get("trending"))).collect(Collectors.toList());
    });
  }

  @Override
  public Article findById(ArticleId articleId) throws ArticleNotFoundException {
    return jdbi.inTransaction((Handle handle) -> {
      try {
        Map<String, Object> result = handle.createQuery("SELECT id, name, trending FROM article WHERE id = :id")
            .bind("id", articleId.id())
            .mapToMap()
            .first();
        return new Article(
            (new ArticleId((Long) result.get("id"))),
            ((String) result.get("name"))).setTrending((boolean) result.get("trending"));
      } catch (IllegalStateException e) {
        throw new ArticleNotFoundException("Have no article with such id");
      }
    });
  }

  @Override
  public long create(String name) {
    return jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate(
          "INSERT INTO article (name, trending) VALUES (:name, FALSE)")
          .bind("name", name)
          .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
  }

  @Override
  public synchronized long update(String name, ArticleId articleId) throws ArticleNotFoundException {
    return jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate(
          "UPDATE article SET name = :name WHERE id = :articleId")
          .bind("name", name)
          .bind("articleId", articleId.id())
          .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
  }

  @Override
  public long delete(ArticleId articleId) throws ArticleNotFoundException {
    return jdbi.inTransaction((Handle handle) -> {
      try {
        ResultBearing resultBearing = handle.createUpdate(
            "DELETE FROM article WHERE id = :articleId")
            .bind("articleId", articleId.id())
            .executeAndReturnGeneratedKeys("id");
        Map<String, Object> mapResult = resultBearing.mapToMap().first();
        return ((Long) mapResult.get("id"));
      } catch (IllegalStateException e) {
        throw new ArticleNotFoundException("");
      }
    });
  }

  @Override
  public long addComment(ArticleId articleId, String text) {
    long id = jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate(
          "INSERT INTO comment (article_id, text) VALUES (:articleId, :text)")
          .bind("articleId", articleId.id())
          .bind("text", text)
          .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
    updateTrending(articleId);
    return id;
  }

  @Override
  public long deleteCommentFromArticle(ArticleId articleId, CommentId commentId) {
    long id = jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate(
          "DELETE FROM comment WHERE id = :commentId")
          .bind("commentId", commentId.id())
          .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
    updateTrending(articleId);
    return id;
  }

  private void updateTrending(ArticleId articleId) {
    jdbi.inTransaction((Handle ownHandle) -> {
      boolean trending = jdbi.inTransaction((Handle handle) -> {
        Map<String, Object> result = handle.createQuery("""
            SELECT COUNT(*) FROM comment
            LEFT JOIN article ON article.id = comment.article_id
            where article.id = :articleId""")
            .bind("articleId", articleId.id())
            .mapToMap()
            .first();
        return (Long) result.get("count") >= 3;
      });
      jdbi.inTransaction((Handle handle) -> {
        handle.createUpdate("UPDATE article SET trending = :trending WHERE id = :articleId")
            .bind("trending", trending)
            .bind("articleId", articleId.id())
            .execute();
        return null;
      });
      return null;
    });
  }
}
