INSERT INTO ppurigi(id, send_user_id, room_id, amount, req_cnt, reg_dts, exp_dts, token)
VALUES (1, 163232, 'room_1', 10000, 2, '2021-05-01 09:00:00', '2021-05-01 09:10:00', 'ABC'),
       (2, 171187, 'room_2', 10000, 3, '2021-05-01 09:00:00', '2021-05-01 09:10:00', 'DEF')
;

INSERT INTO ppurigi_dtlc(id, seq, amount, receive_user_id, reg_dts, mod_dts, version)
VALUES (1, 1, 7000, 163233, '2021-05-01 09:00:00', '2021-05-01 10:00:00', 1),
       (1, 2, 3000, 163234, '2021-05-01 09:00:00', '2021-05-01 10:00:00', 1),
       (2, 1, 3000, null, '2021-05-01 09:00:00', '2021-05-01 10:00:00', 1),
       (2, 2, 3000, null, '2021-05-01 09:00:00', '2021-05-01 10:00:00', 1),
       (2, 3, 4000, null, '2021-05-01 09:00:00', '2021-05-01 10:00:00', 1)
;