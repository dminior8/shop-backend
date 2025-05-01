-- Przykładowe dane
INSERT INTO products (id, name, description, price, available_qty) VALUES
                                                                       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Laptop Gaming', '15.6", i7, 16GB RAM, RTX 3060', 5499.00, 10),
                                                                       ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Smartphone Flagship', '6.7", 256GB, 5G', 3999.00, 25),
                                                                       ('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Konsola Game', 'Next-gen 1TB', 2499.00, 15),
                                                                       ('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Słuchawki Bezprzewodowe', 'ANC, 50h playback', 699.00, 50);

INSERT INTO carts (id, user_id, status, version) VALUES
                                                     ('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'f5eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'CHECKED_OUT', 3),
                                                     ('f6eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', '06eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'ACTIVE', 1);

INSERT INTO cart_items (id, cart_id, product_id, quantity, price) VALUES
                                                                      ('17eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 1, 5499.00),
                                                                      ('28eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'd3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 2, 699.00),
                                                                      ('39eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'f6eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 1, 3999.00);

INSERT INTO orders (id, user_id, cart_id, status, total_amount) VALUES
    ('4aeebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'f5eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'COMPLETED', 6897.00);

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
                                                                         ('4aeebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 1, 5499.00),
                                                                         ('4aeebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'd3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 2, 699.00);