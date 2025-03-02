CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    uuid        UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on  TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    version     BIGINT DEFAULT 0 NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255),
    enabled     BOOLEAN      NOT NULL,
    authorities TEXT[]
);