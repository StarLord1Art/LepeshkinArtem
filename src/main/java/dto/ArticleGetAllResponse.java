package dto;

import entity.Article;

import java.util.List;

public record ArticleGetAllResponse(List<Article> articles) {}