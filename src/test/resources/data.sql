TRUNCATE test_translateit.public.donation,
test_translateit.public.wordbook,
test_translateit.public.feedback,
test_translateit.public.token,
test_translateit.public.account
RESTART IDENTITY CASCADE;

-- @see zjp.translateit.TestConstant
INSERT INTO test_translateit.public.account (uid, name, password, email, status)
VALUES (10000000000001, 'test', 'test@example.com', '$2a$10$dUfp2uqdJLTb', 0);

INSERT INTO donation (trade, time, name, amount, comment)
VALUES ('20180101000000000000000000000000', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000001', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000002', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000003', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000004', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000005', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000006', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000007', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000008', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000009', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000010', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000011', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000012', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000013', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000014', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment'),
       ('20180101000000000000000000000015', '2018-02-19 06:25:20.000000 +08:00', 'name', 100, 'comment');