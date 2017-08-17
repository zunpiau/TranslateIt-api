DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS wordbooks;
CREATE TABLE user
(
  id       INT AUTO_INCREMENT
    PRIMARY KEY,
  name     VARCHAR(16)  NOT NULL,
  password VARCHAR(128) NOT NULL,
  email    VARCHAR(64)  NOT NULL,
  status   TINYINT      NOT NULL,
  UNIQUE (name, email)
)
  CHARSET utf8mb4;

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
  means    VARCHAR(1024) DEFAULT NULL,
  exchange VARCHAR(1024) DEFAULT NULL,
  sentence VARCHAR(4096) DEFAULT NULL,
  note     VARCHAR(1024) DEFAULT NULL,
  category VARCHAR(32)   DEFAULT NULL
)
  CHARSET utf8mb4;

