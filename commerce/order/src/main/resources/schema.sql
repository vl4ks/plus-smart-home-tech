CREATE TABLE IF NOT EXISTS orders
(
    order_id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username         VARCHAR,
    shopping_cart_id UUID NOT NULL,
    payment_id       UUID,
    delivery_id      UUID,
    state            VARCHAR(50),
    delivery_weight  DOUBLE PRECISION,
    delivery_volume  DOUBLE PRECISION,
    fragile          BOOLEAN,
    total_price      NUMERIC(19,4),
    delivery_price   NUMERIC(19,4),
    product_price    NUMERIC(19,4)
);

CREATE TABLE IF NOT EXISTS order_items
(
    order_id   UUID REFERENCES orders (order_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity   INTEGER
);