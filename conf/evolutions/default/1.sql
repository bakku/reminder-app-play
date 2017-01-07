# Users schema

# --- !Ups

CREATE EXTENSION pgcrypto;

CREATE TABLE users (
  id        bigserial    NOT NULL, 
  email     varchar(255) NOT NULL,
  password  varchar(255) NOT NULL,
  is_admin  boolean      NOT NULL,
  PRIMARY KEY(id)
);

# --- !Downs

DROP EXTENSION pgcrypto;

DROP TABLE users;
