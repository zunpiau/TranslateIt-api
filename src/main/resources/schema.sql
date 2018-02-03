DROP TABLE IF EXISTS wordbook;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS invite_code;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS account;

CREATE OR REPLACE FUNCTION set_to_now()
  RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$;

CREATE TABLE account
(
  id         SERIAL PRIMARY KEY,
  create_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  uid        BIGINT       NOT NULL,
  name       VARCHAR(16)  NOT NULL,
  password   VARCHAR(128) NOT NULL,
  email      VARCHAR(32)  NOT NULL,
  status     SMALLINT     NOT NULL,
  UNIQUE (uid),
  UNIQUE (NAME),
  UNIQUE (email)
);

CREATE TRIGGER account_updated
  BEFORE UPDATE
  ON account
  FOR EACH ROW EXECUTE PROCEDURE set_to_now();

CREATE TABLE wordbook
(
  id         SERIAL PRIMARY KEY,
  create_at  TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
  uid        BIGINT      NOT NULL,
  word       VARCHAR(64) NOT NULL,
  phEn       VARCHAR(64)   DEFAULT NULL,
  phAm       VARCHAR(64)   DEFAULT NULL,
  phEnUrl    VARCHAR(128)  DEFAULT NULL,
  phAmUrl    VARCHAR(128)  DEFAULT NULL,
  mean       VARCHAR(1024) DEFAULT NULL,
  exchange   VARCHAR(1024) DEFAULT NULL,
  sentence   VARCHAR(4096) DEFAULT NULL,
  note       VARCHAR(1024) DEFAULT NULL,
  category   VARCHAR(32)   DEFAULT NULL,
  UNIQUE (uid, word),
  FOREIGN KEY (uid) REFERENCES account (uid)
);
CREATE INDEX ON wordbook (uid);
CREATE TRIGGER wordbook_updated
  BEFORE UPDATE
  ON wordbook
  FOR EACH ROW EXECUTE PROCEDURE set_to_now();

CREATE TABLE token
(
  id         SERIAL PRIMARY KEY,
  create_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  uid        BIGINT   NOT NULL,
  time       INTEGER  NOT NULL,
  sign       CHAR(64) NOT NULL,
  used       BOOLEAN  NOT NULL,
  FOREIGN KEY (uid) REFERENCES account (uid)
);
CREATE INDEX ON token (uid);
CREATE TRIGGER token_updated
  BEFORE UPDATE
  ON token
  FOR EACH ROW EXECUTE PROCEDURE set_to_now();

CREATE TABLE invite_code
(
  id         SERIAL PRIMARY KEY,
  create_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  uid        BIGINT  NOT NULL,
  code       CHAR(8) NOT NULL,
  user_id    BIGINT      DEFAULT 0,
  UNIQUE (code),
  FOREIGN KEY (uid) REFERENCES account (uid)
);
CREATE INDEX ON invite_code (uid);
CREATE TRIGGER invite_code_updated
  BEFORE UPDATE
  ON invite_code
  FOR EACH ROW EXECUTE PROCEDURE set_to_now();

CREATE TABLE feedback
(
  id         SERIAL PRIMARY KEY,
  create_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  content    VARCHAR(1024),
  contact    VARCHAR(64),
  ua         VARCHAR(512)
);
CREATE TRIGGER feedback_updated
  BEFORE UPDATE
  ON feedback
  FOR EACH ROW EXECUTE PROCEDURE set_to_now();