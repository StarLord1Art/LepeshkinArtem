package org.example.repository.exception;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(String message) {
    super(message);
  }

  public ArticleNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
