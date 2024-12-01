package org.example.service.exception;

public class CommentDeleteException extends RuntimeException {

  public CommentDeleteException(String message, Throwable cause) {
    super(message, cause);
  }
}
