-- Avoiding typical mistakes and explicit locking
-- (избежание типичных ошибок и явных блокировок)

begin;
drop table if exists product;
create table product (id int primary key not null);
commit;

--             Transaction 1                       Transaction 2
    BEGIN;                                  BEGIN;
    SELECT max(id) FROM product;            SELECT max(id) FROM product;
--     User will see 17                         User will see 17
--     User will decide to use 18               User will decide to use 18
    INSERT INTO product (id) VALUES (18);   INSERT INTO product (id) VALUES (18);
    COMMIT;                                 COMMIT;

-- Один из путей решения, явная блокировка таблицы:
--          ACCESS SHARE    | ROW SHARE | ROW EXCLUSIVE |
--   SHARE UPDATE EXCLUSIVE | SHARE     |
--   SHARE ROW EXCLUSIVE    | EXCLUSIVE | ACCESS EXCLUSIVE

-- ACCESS SHARE (блокировка при чтении):
--      Этот тип блокировки используется при чтении и
--      конфликтует только с ACCESS EXCLUSIVE, который
--      устанавливается для DROP TABLE и т.п.
--      Фактически это означает, что SELECT не может выполняться,
--      если таблица удалена. Это также означает, что
--      DROP TABLE должна ждать завершения транзакции чтения.

-- ROW SHARE (блокировка выборки на обновление/чтение):
--      PostgreSQL берет такую блокировку в случае
--      SELECT FOR UPDATE / SELECT FOR SHARE.
--      Он конфликтует с EXCLUSIVE и ACCESS EXCLUSIVE.

-- ROW EXCLUSIVE (блокировка на вставку/обновление/удаление):
--      Эта блокировка используется командами INSERT, UPDATE и DELETE.
--      Он конфликтует с SHARE, SHARE ROW EXCLUSIVE, EXCLUSIVE и ACCESS EXCLUSIVE.

-- SHARE UPDATE EXCLUSIVE ():
--      Этот вид блокировки используется CREATE INDEX CONCURRENTLY,
--      ANALYZE, ALTER TABLE, VALIDATE и некоторыми другими разновидностями
--      ALTER TABLE, а также VACUUM (не VACUUM FULL).
--      Конфликтует с режимами блокировки SHARE UPDATE EXCLUSIVE,
--      SHARE, SHARE ROW EXCLUSIVE, EXCLUSIVE и ACCESS EXCLUSIVE.

-- SHARE:
--      Используется при создании индекса.
--      Он конфликтует с ROW EXCLUSIVE, SHARE UPDATE EXCLUSIVE,
--      SHARE ROW EXCLUSIVE, EXCLUSIVE и ACCESS EXCLUSIVE.

-- SHARE ROW EXCLUSIVE:
--      Используется при выполнении CREATE TRIGGER и некоторыми формами
--      ALTER TABLE и конфликтует со всеми остальными режимами блокировок,
--      кроме ACCESS SHARE.

-- EXCLUSIVE:
--      Этот тип блокировки на сегодняшний день является наиболее строгим.
--      Он защищает как от чтения, так и от записи.
--      Если эта блокировка используется транзакцией,
--      никто другой не сможет читать или писать в затронутую таблицу.

-- ACCESS EXCLUSIVE (блокировка на чтение/запись):
--      Эта блокировка предотвращает чтение и запись одновременных транзакций.

begin;
lock table product in access exclusive mode;
insert into product select max(id) + 1 from product;
commit;

select * from product;

-- Имейте в виду, что это довольно неприятный способ
-- выполнения такой операции, потому что никто другой
-- не может читать или записывать в таблицу во время вашей операции.
-- Поэтому ACCESS EXCLUSIVE следует избегать любой ценой.