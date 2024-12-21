package repositories;

import Exeptions.CommentNotFoundException;
import entity.ArticleId;
import entity.Comment;
import entity.CommentId;

import java.util.List;

public interface CommentRepositoryInterface {

  List<Comment> findAllCommentsOfUser(ArticleId articleId);

  Comment findById(CommentId commentId) throws CommentNotFoundException;

  long update(Comment comment) throws CommentNotFoundException;
}
