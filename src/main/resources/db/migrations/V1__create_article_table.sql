CREATE TABLE article
(
    id   BIGSERIAL PRIMARY KEY,
    trending BOOLEAN DEFAULT false,
    name TEXT NOT NULL
);