package repositories;

import Exeptions.ArticleNotFoundException;
import Exeptions.CommentNotFoundException;
import entity.Article;
import entity.ArticleId;
import entity.Comment;
import entity.CommentId;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
class ArticleRepositoryTest {

  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  private static Jdbi jdbi;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway =
            Flyway.configure()
                    .outOfOrder(true)
                    .locations("classpath:db/migrations")
                    .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                    .load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void beforeEach() {
    jdbi.inTransaction((Handle ownHandle) -> {
      jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
      jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
      return null;
    });
  }

  @Test
  void findAll() {
    ArticleRepository repository = new ArticleRepository(jdbi);

    long[] arrayOfId = new long[3];
    arrayOfId[0] = repository.create("new article1");
    arrayOfId[1] = repository.create("new article2");
    arrayOfId[2] = repository.create("new article3");

    List<Article> articles = repository.findAll();
    for (int i = 1; i < 4; i++) {
      Article article = articles.get(i - 1);
      assertEquals("new article" + i, article.getName());
      assertEquals(arrayOfId[i - 1], article.getId().id());
    }
  }

  @Test
  void findByIdWrongIdTest() {
    ArticleRepository repository = new ArticleRepository(jdbi);

    repository.create("new article");
    try {
      repository.findById(new ArticleId(100));
    } catch (ArticleNotFoundException e) {
      assert (true);
      return;
    }
    assert (false);
  }

  @Test
  void create() throws ArticleNotFoundException {
    ArticleRepository repository = new ArticleRepository(jdbi);

    long orgId = repository.create("new article");

    Article article = repository.findById(new ArticleId(orgId));
    assertEquals("new article", article.getName());
    assertEquals(orgId, article.getId().id());
  }

  @Test
  void update() throws ArticleNotFoundException {
    ArticleRepository repository = new ArticleRepository(jdbi);

    long artId = repository.create("new article");

    long newArtId = repository.update("update article", new ArticleId(artId));

    Article article = repository.findById(new ArticleId(newArtId));

    assertEquals("update article", article.getName());
    assertEquals(newArtId, article.getId().id());
  }

  @Test
  void delete() throws ArticleNotFoundException {
    ArticleRepository repository = new ArticleRepository(jdbi);

    long artId = repository.create("new article");

    repository.delete(new ArticleId(artId));

    try {
      repository.findById(new ArticleId(artId));
    } catch (ArticleNotFoundException e) {
      assert (true);
      return;
    }
    assert (false);
  }

  @Test
  void addComment() throws CommentNotFoundException {
    ArticleRepository repository = new ArticleRepository(jdbi);
    CommentRepository commentRepository = new CommentRepository(jdbi);

    long artId = repository.create("new article");

    long commentId = repository.addComment(new ArticleId(artId), "new comment");

    Comment comment = commentRepository.findById(new CommentId(commentId));

    assertEquals("new comment", comment.getText());
    assertEquals(commentId, comment.getId().id());
  }

  @Test
  void deleteCommentFromArticle() {
    ArticleRepository repository = new ArticleRepository(jdbi);
    CommentRepository commentRepository = new CommentRepository(jdbi);

    long artId = repository.create("new article");

    long commentId = repository.addComment(new ArticleId(artId), "new comment");
    repository.deleteCommentFromArticle(new ArticleId(artId), new CommentId(commentId));

    try {
      String text = commentRepository.findById(new CommentId(commentId)).getText();
      System.out.println(text);
    } catch (CommentNotFoundException e) {
      assert (true);
      return;
    }
    assert (false);
  }

  @Test
  void updateTrending() throws ArticleNotFoundException {
    ArticleRepository repository = new ArticleRepository(jdbi);

    long artId = repository.create("trending article");

    repository.addComment(new ArticleId(artId), "new comment1");
    repository.addComment(new ArticleId(artId), "new comment2");
    repository.addComment(new ArticleId(artId), "new comment3");

    long emptyArtId = repository.create("empty article");

    Article article = repository.findById(new ArticleId(artId));
    Article emptyArticle = repository.findById(new ArticleId(emptyArtId));

    assert (article.isTrending());
    assert (!emptyArticle.isTrending());
  }
}