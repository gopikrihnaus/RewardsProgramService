INSERT INTO Customer (id, name, surname, email, created_at, updated_at)
VALUES (1, 'jhon', 'kellner', 'jhon@gmail.com', '2022-01-08 11:58:34', '2022-01-08 11:58:34');
INSERT INTO Customer (id, name, surname, email, created_at, updated_at)
VALUES (2, 'andy', 'eberhard', 'andy@gmail.com', '2022-01-08 11:58:34', '2022-01-08 11:58:34');
INSERT INTO Customer (id, name, surname, email, created_at, updated_at)
VALUES (3, 'test1', 't1', 'test@gmail.com', '2022-01-08 11:58:34', '2022-01-08 11:58:34');
INSERT INTO Customer (id, name, surname, email, created_at, updated_at)
VALUES (4, 'test2', 't2', 'test@gmail.com', '2022-01-08 11:58:34', '2022-01-08 11:58:34');

INSERT INTO Purchase (id, details, amount, customer_id, created_at, updated_at)
VALUES (1, 'Grocery', 120, 1, '2022-06-08 11:58:34', '2022-06-08 11:58:34');
INSERT INTO Purchase (id, details, amount, customer_id, created_at, updated_at)
VALUES (2, 'Grocery', 80, 1, '2022-08-08 11:58:34', '2022-08-08 11:58:34');
INSERT INTO Purchase (id, details, amount, customer_id, created_at, updated_at)
VALUES (3, 'Grocery', 120, 1, '2022-07-28 11:58:34', '2022-07-08 11:58:34');
INSERT INTO Purchase (id, details, amount, customer_id, created_at, updated_at)
VALUES (4, 'Grocery', 100, 1, '2022-08-08 11:58:34', '2022-08-08 11:58:34');

COMMIT;