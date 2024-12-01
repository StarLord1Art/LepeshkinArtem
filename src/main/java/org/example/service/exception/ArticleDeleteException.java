package org.example.service.exception;

public class ArticleDeleteException extends RuntimeException {

  public ArticleDeleteException(String message, Throwable cause) {
    super(message, cause);
  }
}
