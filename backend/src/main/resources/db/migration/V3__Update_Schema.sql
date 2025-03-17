ALTER TABLE User
    DROP COLUMN role;

CREATE TABLE UserRole (
    id          int         NOT NULL    AUTO_INCREMENT,
    role_name   varchar(50) NOT NULL,
    user_id     int         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);