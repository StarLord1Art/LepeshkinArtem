package org.example.repository.exception;

public class CommentIdDuplicatedException extends RuntimeException {
  public CommentIdDuplicatedException(String message) {
    super(message);
  }

  public CommentIdDuplicatedException(String message, Throwable cause) {
    super(message, cause);
  }
}
