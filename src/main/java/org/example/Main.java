package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.ArticleController;
import org.example.controller.ArticleFreemarkerController;
import org.example.controller.CommentController;
import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.InMemoryArticleRepository;
import org.example.repository.InMemoryCommentRepository;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.template.TemplateFactory;
import spark.Service;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository();
    CommentRepository commentRepository = new InMemoryCommentRepository();

    final ArticleService articleService = new ArticleService(articleRepository);
    final CommentService commentService = new CommentService(commentRepository, articleRepository);

    Application application =
        new Application(
            List.of(
                new ArticleController(service, articleService, objectMapper),
                new ArticleFreemarkerController(
                    service, articleService, TemplateFactory.freeMarkerEngine()),
                new CommentController(service, commentService, objectMapper)));
    application.start();
  }
}
