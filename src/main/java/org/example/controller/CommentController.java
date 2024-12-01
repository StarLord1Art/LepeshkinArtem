package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.controller.request.CommentCreateRequest;
import org.example.controller.request.CommentDeleteRequest;
import org.example.controller.response.ErrorResponse;
import org.example.controller.response.CommentCreateResponse;
import org.example.controller.response.CommentDeleteResponse;

import org.example.entity.CommentId;

import org.example.service.CommentService;
import org.example.service.exception.CommentCreateException;
import org.example.service.exception.CommentDeleteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

  private final Service service;
  private final CommentService commentService;
  private final ObjectMapper objectMapper;

  public CommentController(
      Service service, CommentService commentService, ObjectMapper objectMapper) {
    this.service = service;
    this.commentService = commentService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createComment();
    deleteComment();
  }

  private void createComment() {
    service.post(
        "/api/comments",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          CommentCreateRequest commentCreateRequest =
              objectMapper.readValue(body, CommentCreateRequest.class);
          try {
            CommentId commentId =
                commentService.create(
                    commentCreateRequest.articleId(), commentCreateRequest.text());
            response.status(201);
            LOG.debug("Comment created");
            return objectMapper.writeValueAsString(new CommentCreateResponse(commentId));
          } catch (CommentCreateException e) {
            LOG.warn("Cannot create comment", e);
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void deleteComment() {
    service.delete(
        "/api/comments/:commentId",
        (Request request, Response response) -> {
          response.type("application/json");
          CommentId commentId = new CommentId(Long.parseLong(request.params("commentId")));
          CommentDeleteRequest commentDeleteRequest = new CommentDeleteRequest(commentId);
          try {
            commentService.delete(commentDeleteRequest.commentId());
            response.status(200);
            LOG.debug("Comment deleted");
            return objectMapper.writeValueAsString(new CommentDeleteResponse());
          } catch (CommentDeleteException e) {
            LOG.warn("Cannot find comment", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }
}
