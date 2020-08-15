ALTER DATABASE vertx_db SET timezone TO 'America/Bogota';

CREATE SCHEMA products;

CREATE TABLE products.products (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    stock FLOAT8 NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

INSERT INTO products.products
    VALUES ('5ae383b2-bbfc-11ea-8001-3af9d3b25422', 'Test', 20, now());