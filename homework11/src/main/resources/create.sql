DROP TABLE IF EXISTS "address" CASCADE;
DROP TABLE IF EXISTS "phone" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE "user"
(
  id         BIGSERIAL PRIMARY KEY NOT NULL,
  name       VARCHAR DEFAULT NULL,
  age        INT DEFAULT 0         NOT NULL,
  address_id BIGINT
);
CREATE UNIQUE INDEX user_id_uindex
  ON "user" (id);

CREATE TABLE "phone"
(
  id BIGSERIAL PRIMARY KEY,
  phone VARCHAR,
  user_id BIGINT,
  CONSTRAINT phone_user_id_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);
CREATE UNIQUE INDEX phone_id_uindex ON "phone" (id);

CREATE TABLE "address"
(
  id BIGSERIAL PRIMARY KEY,
  address VARCHAR,
  user_id BIGINT,
  CONSTRAINT address_user_id_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);
CREATE UNIQUE INDEX address_id_uindex ON "address" (id);

ALTER TABLE "user"
  ADD CONSTRAINT user_address_id_fk
FOREIGN KEY (address_id) REFERENCES address (id);