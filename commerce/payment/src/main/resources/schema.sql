CREATE TABLE IF NOT EXISTS payments
(
    payment_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id       UUID NOT NULL,
    products_total NUMERIC(19,4),
    delivery_total NUMERIC(19,4),
    total_payment  NUMERIC(19,4),
    fee_total      NUMERIC(19,4),
    status         VARCHAR(15)
);