CREATE TABLE IF NOT EXISTS payments
(
    payment_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id       UUID NOT NULL,
    products_total DOUBLE PRECISION,
    delivery_total DOUBLE PRECISION,
    total_payment  DOUBLE PRECISION,
    fee_total      DOUBLE PRECISION,
    status         VARCHAR(15)
);