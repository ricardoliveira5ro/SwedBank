-- DUMMY TEST DATA

INSERT INTO T_USER (id, name, created_at, updated_at)
VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'John Doe', NOW(), NOW());

INSERT INTO T_ACCOUNT (id, user_id, currency, balance, created_at, updated_at)
VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'EUR', 1000.00, NOW(), NOW());

INSERT INTO T_ACCOUNT (id, user_id, currency, balance, created_at, updated_at)
VALUES ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'USD', 500.00, NOW(), NOW());
