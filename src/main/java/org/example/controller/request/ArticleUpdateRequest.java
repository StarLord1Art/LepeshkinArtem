package org.example.controller.request;

import java.util.List;
import java.util.Set;

import org.example.entity.ArticleId;
import org.example.entity.Comment;

public record ArticleUpdateRequest(
    ArticleId articleId, String name, Set<String> tags, List<Comment> comments) {}
