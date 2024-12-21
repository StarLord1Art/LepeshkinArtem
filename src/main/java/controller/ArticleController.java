package controller;

import dto.*;
import Exeptions.ArticleCreateException;
import Exeptions.ArticleDeleteException;
import Exeptions.ArticleFindException;
import Exeptions.ArticleUpdateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Article;
import entity.ArticleId;
import entity.CommentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.List;

public class ArticleController implements Controller {

  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

  private final Service service;
  private final ArticleService articleService;
  private final ObjectMapper objectMapper;

  public ArticleController(Service service, ArticleService articleService, ObjectMapper objectMapper) {
    this.service = service;
    this.articleService = articleService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createArticle();
    getAllArticles();
    getArticle();
    updateArticle();
    deleteArticle();
    createCommentToArticle();
    deleteCommentToArticle();
    createSomeArticles();
  }

  private void createSomeArticles() {
    service.post("/api/articles/someArticles", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();

      SomeArticlesCreateRequest someArticlesCreateRequest = objectMapper.readValue(body, SomeArticlesCreateRequest.class);
      List<Long> ides = articleService.createArticles(someArticlesCreateRequest.names());

      response.status(201);

      return objectMapper.writeValueAsString(new SomeArticlesCreateResponse(ides));
    });
  }

  private void createArticle() {
    service.post("/api/articles", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      ArticleCreateRequest articleCreateRequest = objectMapper.readValue(body, ArticleCreateRequest.class);
      try {
        long articleId = articleService.create(articleCreateRequest.name());
        response.status(201);
        return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
      } catch (ArticleCreateException e) {
        response.status(400);
        return objectMapper.writeValueAsString(new Error(e.getMessage()));
      }
    });
  }

  private void getAllArticles() {
    service.get("/api/articles/getall", (Request request, Response response) -> {
      response.type("application/json");
      List<Article> articles = articleService.findAll();
      response.status(201);
      return objectMapper.writeValueAsString(new ArticleGetAllResponse(articles));
    });
  }

  private void getArticle() {
    service.get("/api/articles/get/:articleId", (Request request, Response response) -> {
      response.type("application/json");
      ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
      try {
        Article article = articleService.findById(articleId);
        response.status(201);
        return objectMapper.writeValueAsString(new ArticleGetResponse(article));
      } catch (ArticleFindException e) {
        response.status(400);
        return objectMapper.writeValueAsString(new Error(e.getMessage()));
      }
    });
  }

  private void updateArticle() {
    service.put("/api/articles/update", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      ArticlePutRequest articlePutRequest = objectMapper.readValue(body, ArticlePutRequest.class);
      try {
        articleService.update(new ArticleId(Integer.parseInt(articlePutRequest.articleId())), articlePutRequest.name());
        response.status(201);
        return objectMapper.writeValueAsString(new ArticlePutResponse(articlePutRequest.articleId()));
      } catch (ArticleUpdateException e) {
        response.status(400);
        return objectMapper.writeValueAsString(new Error(e.getMessage()));
      }
    });
  }

  private void deleteArticle() {
    service.delete("/api/articles/delete/:articleId", (Request request, Response response) -> {
      response.type("application/json");
      ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
      try {
        articleService.delete(articleId);
        response.status(201);
        return objectMapper.writeValueAsString(new ArticleDeleteResponse(articleId.id() + ""));
      } catch (ArticleDeleteException e) {
        response.status(400);
        return objectMapper.writeValueAsString(new Error(e.getMessage()));
      }
    });
  }

  private void createCommentToArticle() {
    service.post("/api/comments", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      ArticleCreateCommentRequest articleCreateCommentRequest = objectMapper.readValue(body, ArticleCreateCommentRequest.class);
      long commentId = articleService.createComment(new ArticleId(Integer.parseInt(articleCreateCommentRequest.articleId())), articleCreateCommentRequest.text());
      response.status(201);
      return objectMapper.writeValueAsString(new ArticleCreateCommentResponse(String.valueOf(commentId)));
    });
  }

  private void deleteCommentToArticle() {
    service.delete("/api/comments/delete/:articleId/:commentId", (Request request, Response response) -> {
      response.type("application/json");
      ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
      CommentId commentId = new CommentId(Long.parseLong(request.params("commentId")));
      try {
        articleService.deleteComment(articleId, commentId);
        response.status(201);
        return objectMapper.writeValueAsString(new ArticleDeleteCommentResponse(articleId.id() + ""));
      } catch (ArticleFindException e) {
        response.status(400);
        return objectMapper.writeValueAsString(new Error(e.getMessage()));
      }
    });
  }
}