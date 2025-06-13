DROP TABLE IF EXISTS warehouse_product, order_booking, order_booking_products;

CREATE TABLE IF NOT EXISTS warehouse_product (
    product_id UUID PRIMARY KEY,
    weight DOUBLE PRECISION,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    fragile BOOLEAN,
    quantity INTEGER
);

create table if not exists order_booking
(
    order_booking_id UUID PRIMARY KEY,
    order_id         UUID,
    delivery_id      UUID,
    delivery_weight  DOUBLE PRECISION NOT NULL,
    delivery_volume  DOUBLE PRECISION NOT NULL,
    fragile          BOOLEAN NOT NULL
);

create table if not exists order_booking_products
(
    order_booking_id UUID PRIMARY KEY,
    product_id       UUID,
    quantity         BIGINT
);