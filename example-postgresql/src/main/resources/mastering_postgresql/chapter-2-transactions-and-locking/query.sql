select now(), now();

begin;

select now(), now();

commit;

-- Handling errors inside a transaction
begin;
select 1;
select 1 / 0;
select 1;
commit;
rollback;

-- Making use of SAVEPOINT
begin;
select 1;
savepoint a;
select 2 / 0;
select 2;
rollback to savepoint a;
select 3;
commit;


-- Transactional DDLs
begin;
create table t_test
(
    id int
);
alter table t_test
    alter column id type int8;
rollback;

select column_name, data_type, character_maximum_length, column_default, is_nullable
from INFORMATION_SCHEMA.COLUMNS
where table_name = 't_test';

-- Understanding basic locking
drop table if exists t_test;
create table t_test
(
    id int
);
insert into t_test
values (1);

-- Несколько пользователей могут читать одни и
-- те же данные одновременно, не блокируя друг друга.

-- Пишущие транзакции не будут блокировать читающие транзакции.
-- transaction 1
begin;
update t_test set id = id + 1 returning *;
-- user will see 1
commit;

-- transaction 2
begin;
select * from t_test;
-- user will see 1
commit;

-- Пишущие параллельные транзакции.
-- transaction 1
begin;
update t_test set id = id + 1 returning *;
select * from t_test;
-- it will return 3
commit;

-- transaction 2
begin;
select * from t_test;
update t_test set id = id + 1 returning *;
-- it will wait for transaction 1
-- it will wait for transaction 1
-- It will reread the row, find 3, set the value, and return 4
commit;

-- Также примечательно, что вы всегда можете запускать одновременные чтения.
-- Наши две записи не будут блокировать чтение.