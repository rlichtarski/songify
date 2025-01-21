CREATE SEQUENCE IF NOT EXISTS song_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE song
(
    id           BIGINT       PRIMARY KEY NOT NULL DEFAULT nextval('song_id_seq'),
    name         VARCHAR(255) NOT NULL,
    release_date TIMESTAMP WITHOUT TIME ZONE,
    duration     BIGINT,
    language     VARCHAR(255)
);