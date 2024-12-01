package org.example.service.exception;

public class CommentCreateException extends RuntimeException {

  public CommentCreateException(String message) {
    super(message);
  }

  public CommentCreateException(String message, Throwable cause) {
    super(message, cause);
  }
}
