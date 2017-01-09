# Reminders schema

# --- !Ups

CREATE TABLE reminders (
  id            bigserial    NOT NULL, 
  message       text         NOT NULL,
  reminder_date bigint       NOT NULL,
  user_id       bigint       REFERENCES users(id),
  PRIMARY KEY(id)
);

# --- !Downs

DROP TABLE reminders;
