package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;

import org.example.repository.ArticleRepository;
import org.example.repository.InMemoryArticleRepository;
import org.example.service.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ArticleControllerTest {

  private Service service;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void should201IfArticleIsSuccessfullyCreated() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    Application application =
        new Application(List.of(new ArticleController(service, articleService, objectMapper)));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
																										{"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());
  }

  @Test
  void should200IfArticleIsSuccessfullyUpdated() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    Application application =
        new Application(List.of(new ArticleController(service, articleService, objectMapper)));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
																						{"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());

    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .PUT(
                        HttpRequest.BodyPublishers.ofString(
                            """
																						{"name": "No testing", "tags": ["sci-fi"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(200, responseSecond.statusCode());
  }

  @Test
  void should404IfArticleDoesNotUpdated() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    Application application =
        new Application(List.of(new ArticleController(service, articleService, objectMapper)));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .PUT(
                        HttpRequest.BodyPublishers.ofString(
                            """
																																		{"name": "No testing", "tags": ["sci-fi"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(404, response.statusCode());
  }

  @Test
  void should200IfArticleIsSuccessfullyDeleted() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    Application application =
        new Application(List.of(new ArticleController(service, articleService, objectMapper)));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
																																		{"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());

    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(200, responseSecond.statusCode());
  }

  @Test
  void should404IfArticleDoesNotDeleted() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    Application application =
        new Application(List.of(new ArticleController(service, articleService, objectMapper)));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(404, responseSecond.statusCode());
  }
}
