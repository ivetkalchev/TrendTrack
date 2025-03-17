CREATE TABLE Cart (
    id          int             NOT NULL    AUTO_INCREMENT,
    user_id     int             NOT NULL,
    total_cost  decimal(10, 2)  NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE CartItem (
    id          int             NOT NULL    AUTO_INCREMENT,
    fabric_id   int             NOT NULL,
    cart_id     int             NOT NULL,
    quantity    int             NOT NULL,
    total_price decimal(10, 2)  NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (fabric_id) REFERENCES Fabric(id),
    FOREIGN KEY (cart_id) REFERENCES Cart(id)
);