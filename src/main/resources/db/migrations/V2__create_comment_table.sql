CREATE TABLE comment
(
    id   BIGSERIAL PRIMARY KEY,
    article_id   BIGSERIAL NOT NULL,
    text TEXT
);