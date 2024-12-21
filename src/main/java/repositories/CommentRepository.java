package repositories;

import Exeptions.CommentNotFoundException;
import entity.ArticleId;
import entity.Comment;
import entity.CommentId;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentRepository implements CommentRepositoryInterface {

  private final Jdbi jdbi;

  public CommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public List<Comment> findAllCommentsOfUser(ArticleId articleId) {
    return jdbi.inTransaction((Handle handle) -> {
      List<Map<String, Object>> result = handle
          .createQuery("SELECT id, text FROM comment WHERE article_id = :articleId")
          .bind("articleId", articleId.id())
          .mapToMap().collectIntoList();
      return result.stream().map(element -> new Comment(
          (new CommentId((Long) element.get("id"))),
          (articleId),
          ((String) element.get("text")))).collect(Collectors.toList());
    });
  }

  @Override
  public Comment findById(CommentId commentId) throws CommentNotFoundException {
    return jdbi.inTransaction((Handle handle) -> {
      try {
        Map<String, Object> result = handle.createQuery("SELECT article_id, text FROM comment WHERE id = :id")
            .bind("id", commentId.id())
            .mapToMap()
            .first();
        return new Comment(
            (commentId),
            (new ArticleId((Long) result.get("article_id"))),
            ((String) result.get("text")));
      } catch (IllegalStateException e) {
        throw new CommentNotFoundException("Have no comment with such id");
      }
    });
  }

  @Override
  public synchronized long update(Comment comment) throws CommentNotFoundException {
    return jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate(
          "UPDATE comment SET text = :text WHERE id = :commentId")
          .bind("text", comment.getText())
          .bind("commentId", comment.getId())
          .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
  }
}
