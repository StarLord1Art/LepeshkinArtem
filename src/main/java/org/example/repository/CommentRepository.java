package org.example.repository;

import org.example.entity.CommentId;
import org.example.entity.Comment;

public interface CommentRepository {
  CommentId generateId();

  void create(Comment comment);

  void delete(CommentId commentId);
}
