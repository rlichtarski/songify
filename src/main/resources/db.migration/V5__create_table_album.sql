CREATE TABLE album
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255),
    release_date TIMESTAMP(6) WITHOUT TIME ZONE
);