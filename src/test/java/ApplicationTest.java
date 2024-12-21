import Exeptions.ArticleFindException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ArticleController;
import entity.Article;
import entity.ArticleId;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import repositories.ArticleRepository;
import service.ArticleService;
import spark.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  static {
    POSTGRES.start();
  }

  private static Jdbi jdbi;

  private Service service;


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
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void findAll() throws IOException, InterruptedException, ArticleFindException {
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleService articleService = new ArticleService(new ArticleRepository(jdbi), jdbi);
    Application application = new Application(List.of(new ArticleController(service, articleService, objectMapper)));
    application.start();
    service.awaitInitialization();
    HttpResponse<String> createArticleResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                        {
                                                          "name": "qwerty"
                                                        }
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:4567/api/articles"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, createArticleResponse.statusCode());
    long articleId = Long.parseLong(
            objectMapper.readValue(createArticleResponse.body(),
                            new TypeReference<Map<String, String>>() {
                            })
                    .get("id"));

    HttpResponse<String> createCommentResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            String.format("""
                                                    {
                                                      "articleId": "%d",
                                                      "text": "123"
                                                    }
                                                    """, articleId)
                                    )
                            )
                            .uri(URI.create("http://localhost:4567/api/comments"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, createCommentResponse.statusCode());

    long commentId = Long.parseLong(
            objectMapper.readValue(createCommentResponse.body(),
                            new TypeReference<Map<String, String>>() {
                            })
                    .get("commentId"));

    HttpResponse<String> articleUpdateRequest = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            String.format("""
                                                    {
                                                      "articleId": "%d",
                                                      "name": "qwerty updated"
                                                    }
                                                    """, articleId)
                                    )
                            )
                            .uri(URI.create("http://localhost:4567/api/articles/update"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, articleUpdateRequest.statusCode());

    HttpResponse<String> deleteCommentResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create(
                                    String.format("http://localhost:4567/api/comments/delete/%d/%d", articleId, commentId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, deleteCommentResponse.statusCode());

    HttpResponse<String> getArticleResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(String.format("http://localhost:4567/api/articles/get/%d", articleId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, getArticleResponse.statusCode());

    Article article = articleService.findById(new ArticleId(1));
    assertEquals(article.getName(), "qwerty updated");
    assertEquals(article.getCountOfComments(), 0);

    HttpResponse<String> deleteArticleRequest = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create(String.format("http://localhost:4567/api/articles/delete/%d", articleId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(201, deleteArticleRequest.statusCode());

    HttpResponse<String> badDeleteArticleRequest = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create(String.format("http://localhost:4567/api/articles/delete/%d", articleId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(400, badDeleteArticleRequest.statusCode());

    HttpResponse<String> badGetArticleResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(String.format("http://localhost:4567/api/articles/get/%d", articleId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(400, badGetArticleResponse.statusCode());

    HttpResponse<String> badDeleteCommentResponse = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create(
                                    String.format("http://localhost:4567/api/comments/delete/%d/%d", articleId, commentId)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(400, badDeleteCommentResponse.statusCode());

    HttpResponse<String> badArticleUpdateRequest = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            String.format("""
                                                    {
                                                      "articleId": "%d",
                                                      "name": "qwerty updated"
                                                    }
                                                    """, articleId)
                                    )
                            )
                            .uri(URI.create("http://localhost:4567/api/articles/update"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(400, badArticleUpdateRequest.statusCode());
  }
}