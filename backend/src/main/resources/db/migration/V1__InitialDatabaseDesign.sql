CREATE TABLE Fabric
(
    id              int             NOT NULL    AUTO_INCREMENT,
    name            varchar(100)    NOT NULL,
    description     varchar(500)    NOT NULL,
    material        varchar(50)     NOT NULL,
    color           varchar(50)     NOT NULL,
    price           decimal(10, 2)  NOT NULL,
    washable        bit             NOT NULL,
    ironed          bit             NOT NULL,
    stock           int             NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE User
(
    id              int             NOT NULL    AUTO_INCREMENT,
    role            varchar(50)     NOT NULL,
    username        varchar(100)    NOT NULL,
    password        varchar(255)    NOT NULL,
    email           varchar(100)    NOT NULL,
    first_name      varchar(100)    NOT NULL,
    last_name       varchar(100)    NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (username)
);