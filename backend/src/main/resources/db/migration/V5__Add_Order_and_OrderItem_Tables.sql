CREATE TABLE Orders (
    id             int              NOT NULL    AUTO_INCREMENT,
    user_id        int              NOT NULL,
    address        varchar(255)     NOT NULL,
    date           date             NOT NULL,
    status         varchar(30)      NOT NULL,
    total_amount   decimal(10, 2)   NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE OrderItem (
    id              int             NOT NULL    AUTO_INCREMENT,
    order_id        int             NOT NULL,
    fabric_id       int             NOT NULL,
    quantity        int             NOT NULL,
    price_per_unit  decimal(10, 2)  NOT NULL,
    total_price     decimal(10, 2)  NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (fabric_id) REFERENCES Fabric(id)
);