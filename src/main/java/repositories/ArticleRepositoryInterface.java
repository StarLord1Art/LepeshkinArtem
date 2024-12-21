package repositories;

import Exeptions.ArticleIdDuplicatedException;
import Exeptions.ArticleNotFoundException;
import entity.Article;
import entity.ArticleId;
import entity.Comment;
import entity.CommentId;

import java.util.List;

public interface ArticleRepositoryInterface {

  List<Article> findAll();

  Article findById(ArticleId articleId) throws ArticleNotFoundException;

  long create(String name) throws ArticleIdDuplicatedException;

  long update(String name, ArticleId articleId) throws ArticleNotFoundException;

  long delete(ArticleId articleId) throws ArticleNotFoundException;

  long addComment(ArticleId articleId, String text);

  long deleteCommentFromArticle(ArticleId articleId, CommentId commentid);
}
