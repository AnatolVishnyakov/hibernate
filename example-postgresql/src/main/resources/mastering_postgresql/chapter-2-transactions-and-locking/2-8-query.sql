-- Making use of FOR SHARE and FOR UPDATE

-- Иногда данные выбираются из базы данных;
-- затем в приложении происходит некоторая обработка и,
-- затем, изменения сохраняются в БД.

begin;
drop table if exists invoice;
create table invoice (id int primary key, processed boolean default 'f');
insert into invoice (id, processed) values (1, 'f');
select * from invoice;
commit;
rollback;


begin;
select * from invoice where processed = false;
update invoice set processed = true where id = 1;
commit;
rollback;

-- Проблема здесь в том, что два человека могут выбрать
-- одни и те же данные. Изменения, внесенные в эти строки,
-- будут перезаписаны. Короче говоря, возникнет состояние гонки.

-- Решением этой проблемы является использование
-- select for update

select * from invoice;
insert into invoice (id, processed) values ((select max(id) + 1 from invoice), 'f');

begin;
select * from invoice where processed = false for update;
update invoice set processed = true;
commit;

-- SELECT FOR UPDATE блокирует строки так же, как и UPDATE.
-- Это означает, что никакие изменения не могут происходить одновременно.
-- Все блокировки будут сняты на COMMIT.
-- Если один SELECT FOR UPDATE ожидает другого SELECT FOR UPDATE,
-- нужно дождаться завершения (COMMIT или ROLLBACK).
-- Если первая транзакция не хочет заканчиваться по какой-либо причине,
-- вторая транзакция потенциально может ждать вечно.
-- Чтобы избежать этого, можно использовать SELECT FOR UPDATE NOWAIT.

set lock_timeout to 5000;

begin;
select * from invoice where processed = false for update nowait;
update invoice set processed = true;
commit;

CREATE TABLE t_flight AS
    SELECT * FROM generate_series(1, 200) AS id;
select * from t_flight;

begin;
select * from t_flight limit 2 for update skip locked;
commit;

-- Помимо FOR UPDATE, есть FOR SHARE, FOR NO KEY UPDATE и FOR KEY SHARE.
-- Следующий листинг описывает, что на самом деле означают эти режимы:

-- FOR NO KEY UPDATE:
--      Очень похоже на FOR UPDATE.
--      Однако блокировка слабее, поэтому она может
--      сосуществовать с SELECT FOR SHARE.

-- FOR SHARE:
--      FOR UPDATE довольно строгий режим и работает в предположении,
--      что вы определенно собираетесь менять строки.
--      FOR SHARE отличается тем, что несколько транзакций
--      могут одновременно удерживать блокировку FOR SHARE.

-- FOR KEY SHARE:
--      Работает аналогично FOR SHARE, за исключением того,
--      что блокировка не строгая. Будет заблокирован для FOR UPDATE,
--      но не будет заблокирован FOR NO KEY UPDATE.