CREATE TABLE products
(
    id            VARCHAR(36) PRIMARY KEY,
    name          VARCHAR(255)             NOT NULL,
    description   TEXT,
    price         NUMERIC(12, 2)           NOT NULL CHECK (price >= 0),
    available_qty INTEGER                  NOT NULL CHECK (available_qty >= 0),
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE carts
(
    id               UUID PRIMARY KEY,
    user_id          UUID                     NOT NULL,
    status           VARCHAR(16)              NOT NULL -- NEW, ACTIVE, CHECKED_OUT, EXPIRED
        CHECK (status IN ('NEW', 'ACTIVE', 'CHECKED_OUT', 'EXPIRED')),
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_modified_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    version          BIGINT                   NOT NULL -- dla @Version (optimistic locking)
);

CREATE TABLE cart_items
(
    id         UUID PRIMARY KEY,
    cart_id    UUID                     NOT NULL,
    product_id UUID                     NOT NULL,
    quantity   INTEGER                  NOT NULL CHECK (quantity > 0),
    price      NUMERIC(12, 2)           NOT NULL CHECK (price >= 0),
    added_at   TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id) REFERENCES carts (id)
            ON DELETE CASCADE
);

CREATE TABLE orders
(
    id           UUID PRIMARY KEY,
    user_id      VARCHAR(36)              NOT NULL,
    cart_id      UUID                     NOT NULL UNIQUE,
    status       VARCHAR(16)              NOT NULL -- PLACED, PROCESSING, COMPLETED, CANCELLED
        CHECK (status IN ('PLACED', 'PROCESSING', 'COMPLETED', 'CANCELLED')),
    total_amount NUMERIC(12, 2)           NOT NULL CHECK (total_amount >= 0),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP WITH TIME ZONE
);

CREATE TABLE order_items
(
    order_id   UUID           NOT NULL,
    product_id VARCHAR(36)    NOT NULL,
    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0),

    PRIMARY KEY (order_id, product_id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE
);


ALTER TABLE orders ADD CONSTRAINT fk_orders_cart FOREIGN KEY (cart_id) REFERENCES carts (id);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);
CREATE UNIQUE INDEX uk_carts_user_id ON carts (user_id);
CREATE INDEX idx_products_available_qty ON products (available_qty);
