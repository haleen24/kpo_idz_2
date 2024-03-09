CREATE TABLE "user"
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(256)       NOT NULL,
    root     VARCHAR(16)        NOT NULL
);

INSERT INTO "user" (login, password, root)
VALUES ('admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'ADMIN');

CREATE TABLE "menu"
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(100) UNIQUE NOT NULL,
    amount_of INTEGER             NOT NULL,
    price     INTEGER             NOT NULL,
    seconds   INTEGER             NOT NULL
);

CREATE TABLE "order"
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER     NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    status  VARCHAR(16) NOT NULL,
    price   INT         NOT NULL,
    time    INT         NOT NULL
);

CREATE TABLE "order_to_dish"
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    order_id INTEGER NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    dish_id  INTEGER NOT NULL REFERENCES "menu" (id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    CONSTRAINT position UNIQUE (order_id, dish_id)
);


CREATE TABLE "review"
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER      NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    dish_id  INTEGER      NOT NULL REFERENCES "menu" (id) ON DELETE CASCADE,
    order_id INTEGER      NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    rate     INTEGER      NOT NULL,
    review   VARCHAR(256) NOT NULL,
    CONSTRAINT signature UNIQUE (user_id, dish_id, order_id)
);
