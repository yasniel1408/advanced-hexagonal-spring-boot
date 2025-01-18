CREATE TABLE public.customer(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL,
    status VARCHAR NOT NULL
);
