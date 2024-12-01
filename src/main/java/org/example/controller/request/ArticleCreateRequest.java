package org.example.controller.request;

import java.util.Set;

public record ArticleCreateRequest(String name, Set<String> tags) {}
