package org.example.controller.request;

import org.example.entity.ArticleId;

public record CommentCreateRequest(ArticleId articleId, String text) {}
