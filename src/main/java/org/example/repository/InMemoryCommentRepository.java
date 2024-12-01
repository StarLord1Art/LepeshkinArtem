package org.example.repository;

import org.example.entity.CommentId;
import org.example.entity.Comment;

import org.example.repository.exception.CommentIdDuplicatedException;
import org.example.repository.exception.CommentNotFoundException;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentRepository implements CommentRepository {

  private final AtomicLong nextId = new AtomicLong(0);
  private final Map<CommentId, Comment> commentsMap = new ConcurrentHashMap<>();

  @Override
  public CommentId generateId() {
    return new CommentId(nextId.incrementAndGet());
  }

  @Override
  public synchronized void create(Comment comment) {
    if (commentsMap.get(comment.getId()) != null) {
      throw new CommentIdDuplicatedException(
          "Comment with the given id already exists: " + comment.getId());
    }
    commentsMap.put(comment.getId(), comment);
  }

  @Override
  public void delete(CommentId commentId) {
    if (commentsMap.remove(commentId) == null) {
      throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
    }
  }
}
