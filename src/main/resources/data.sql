DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS wordbooks;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS feedback;

CREATE TABLE user
(
  id       INT AUTO_INCREMENT
    PRIMARY KEY,
  uid      INT UNIQUE                NOT NULL,
  name     VARCHAR(16) UNIQUE        NOT NULL,
  password VARCHAR(128)              NOT NULL,
  email    VARCHAR(64) UNIQUE        NOT NULL,
  status   TINYINT                   NOT NULL,
  UNIQUE (name, email, uid),
  INDEX (name),
  INDEX (email)
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
  INDEX (uid),
  INDEX (uid, word)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE token
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  uid  INT NOT NULL,
  time LONG,
  sign VARCHAR(64),
  used BOOL,
  INDEX (uid),
  INDEX (uid, sign, used)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE feedback
(
  id      INT AUTO_INCREMENT
    PRIMARY KEY,
  data    TIMESTAMP,
  content VARCHAR(512),
  contact VARCHAR(64),
  ua      VARCHAR(512)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

