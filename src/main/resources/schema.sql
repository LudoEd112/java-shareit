DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS request CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
  user_id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  user_name     VARCHAR(255) NOT NULL,
  user_email    VARCHAR(512) NOT NULL,
  CONSTRAINT    uq_email UNIQUE (user_email)
);

CREATE TABLE IF NOT EXISTS request (
  request_id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  description   VARCHAR(255) NOT NULL,
  requestor_id       INTEGER NOT NULL,
  creation_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT    fk_requestor FOREIGN KEY (requestor_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
  item_id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  item_name     VARCHAR(255) NOT NULL,
  description   VARCHAR(512) NOT NULL,
  owner_id      INTEGER NOT NULL,
  is_available  BOOLEAN NOT NULL,
  request_id    INTEGER,
  CONSTRAINT    fk_owner FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT    fk_request FOREIGN KEY (request_id) REFERENCES requests(request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
  booking_id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  start_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id       INTEGER NOT NULL,
  booker_id   INTEGER NOT NULL,
  status        VARCHAR(255) NOT NULL,
  CONSTRAINT    fk_booker FOREIGN KEY (booker_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT    fk_item_book FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
  comments_id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  text          VARCHAR(255) NOT NULL,
  item_id       INTEGER NOT NULL,
  author_id     INTEGER NOT NULL,
  created       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT    fk_item_com FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE,
  CONSTRAINT    fk_author FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE
);