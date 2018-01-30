DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS wordbook;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS invite_code;
DROP TABLE IF EXISTS feedback;

CREATE TABLE user
(
  id            INT      AUTO_INCREMENT
    PRIMARY KEY,
  time_create   DATETIME DEFAULT CURRENT_TIMESTAMP,
  time_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  uid           BIGINT       NOT NULL,
  name          VARCHAR(16)  NOT NULL,
  password      VARCHAR(128) NOT NULL,
  email         VARCHAR(32)  NOT NULL,
  status        TINYINT      NOT NULL,
  UNIQUE uk_uid(uid),
  UNIQUE uk_name(name),
  UNIQUE uk_email(email)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE wordbook
(
  id            INT           AUTO_INCREMENT
    PRIMARY KEY,
  time_create   DATETIME      DEFAULT CURRENT_TIMESTAMP,
  time_modified DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  uid           BIGINT      NOT NULL,
  word          VARCHAR(64) NOT NULL,
  phEn          VARCHAR(64)   DEFAULT NULL,
  phAm          VARCHAR(64)   DEFAULT NULL,
  phEnUrl       VARCHAR(128)  DEFAULT NULL,
  phAmUrl       VARCHAR(128)  DEFAULT NULL,
  mean          VARCHAR(1024) DEFAULT NULL,
  exchange      VARCHAR(1024) DEFAULT NULL,
  sentence      VARCHAR(4096) DEFAULT NULL,
  note          VARCHAR(1024) DEFAULT NULL,
  category      VARCHAR(32)   DEFAULT NULL,
  INDEX idx_uid(uid),
  UNIQUE uk_uid_word(uid, word)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE token
(
  id            INT      AUTO_INCREMENT
    PRIMARY KEY,
  time_create   DATETIME DEFAULT CURRENT_TIMESTAMP,
  time_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  uid           BIGINT   NOT NULL,
  time          LONG     NOT NULL,
  sign          CHAR(64) NOT NULL,
  used          BOOL     NOT NULL,
  INDEX idx_uid(uid)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE invite_code
(
  id            INT      AUTO_INCREMENT
    PRIMARY KEY,
  time_create   DATETIME DEFAULT CURRENT_TIMESTAMP,
  time_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  uid           BIGINT  NOT NULL,
  code          CHAR(8) NOT NULL,
  user          BIGINT   DEFAULT 0,
  INDEX idx_uid(uid),
  UNIQUE uk_code(code)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE TABLE feedback
(
  id            INT      AUTO_INCREMENT
    PRIMARY KEY,
  time_create   DATETIME DEFAULT CURRENT_TIMESTAMP,
  time_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  content       VARCHAR(1024),
  contact       VARCHAR(64),
  ua            VARCHAR(512)
)
  ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_unicode_ci;