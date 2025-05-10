-- Schema
CREATE TABLE products (
                          id            UUID PRIMARY KEY,
                          name          VARCHAR(255)             NOT NULL,
                          description   TEXT,
                          price         NUMERIC(12, 2)           NOT NULL CHECK (price >= 0),
                          available_qty INTEGER                  NOT NULL CHECK (available_qty >= 0),
                          created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                          updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                          version          BIGINT                   NOT NULL DEFAULT 0
);

CREATE TABLE carts (
                       id               UUID PRIMARY KEY,
                       user_id          UUID                     NOT NULL,
                       status           VARCHAR(16)              NOT NULL CHECK (status IN ('NEW', 'ACTIVE', 'CHECKED_OUT', 'EXPIRED')),
                       created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       version          BIGINT                   NOT NULL DEFAULT 0
);

CREATE TABLE cart_items (
                            id         UUID PRIMARY KEY,
                            cart_id    UUID                     NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
                            product_id UUID                     NOT NULL REFERENCES products(id),
                            quantity   INTEGER                  NOT NULL CHECK (quantity > 0),
                            price      NUMERIC(12, 2)           NOT NULL CHECK (price >= 0),
                            added_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE orders (
                        id           UUID PRIMARY KEY,
                        user_id      UUID                     NOT NULL,
                        cart_id      UUID                     NOT NULL UNIQUE REFERENCES carts(id),
                        status       VARCHAR(16)              NOT NULL CHECK (status IN ('PLACED', 'PROCESSING', 'COMPLETED', 'CANCELLED')),
                        total_amount NUMERIC(12, 2)           NOT NULL CHECK (total_amount >= 0),
                        created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                        updated_at   TIMESTAMP WITH TIME ZONE
);

CREATE TABLE order_items (
                             order_id   UUID           NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             product_id UUID           NOT NULL REFERENCES products(id),
                             quantity   INTEGER        NOT NULL CHECK (quantity > 0),
                             unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0),
                             PRIMARY KEY (order_id, product_id)
);

CREATE TABLE product_reservations (
                                      id           UUID PRIMARY KEY,
                                      cart_id      UUID                  NOT NULL,
                                      product_id   UUID                  NOT NULL,
                                      quantity     INTEGER               NOT NULL CHECK (quantity > 0),
                                      created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),

                                      UNIQUE(cart_id, product_id)
);

-- indeks przyspieszający pobieranie rezerwacji dla koszyka
CREATE INDEX idx_reservations_cart_id ON product_reservations(cart_id);

-- Indeksy
CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);
CREATE INDEX idx_products_available_qty ON products (available_qty);