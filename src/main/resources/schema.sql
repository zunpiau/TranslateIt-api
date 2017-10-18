DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS wordbooks;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS feedback;

CREATE TABLE user
(
  id       INT AUTO_INCREMENT
    PRIMARY KEY,
  uid      INT          NOT NULL,
  name     VARCHAR(16)  NOT NULL,
  password VARCHAR(128) NOT NULL,
  email    VARCHAR(32)  NOT NULL,
  status   TINYINT      NOT NULL,
  UNIQUE uk_uid(uid),
  UNIQUE uk_name(name),
  UNIQUE uk_email(email)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE wordbooks
(
  id       INT           AUTO_INCREMENT
    PRIMARY KEY,
  uid      INT         NOT NULL,
  word     VARCHAR(64) NOT NULL,
  phEn     VARCHAR(64)   DEFAULT NULL,
  phAm     VARCHAR(64)   DEFAULT NULL,
  phEnUrl  VARCHAR(128)  DEFAULT NULL,
  phAmUrl  VARCHAR(128)  DEFAULT NULL,
  mean     VARCHAR(1024) DEFAULT NULL,
  exchange VARCHAR(1024) DEFAULT NULL,
  sentence VARCHAR(4096) DEFAULT NULL,
  note     VARCHAR(1024) DEFAULT NULL,
  category VARCHAR(32)   DEFAULT NULL,
  INDEX idx_uid(uid),
  UNIQUE uk_uid_word(uid, word)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE token
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  uid  INT         NOT NULL,
  time LONG        NOT NULL,
  sign VARCHAR(64) NOT NULL,
  used BOOL        NOT NULL,
  INDEX idx_uid(uid),
  INDEX idx_uid_sign_used(uid, sign, used)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE feedback
(
  id      INT AUTO_INCREMENT
    PRIMARY KEY,
  data    TIMESTAMP,
  content VARCHAR(1024),
  contact VARCHAR(64),
  ua      VARCHAR(512)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;