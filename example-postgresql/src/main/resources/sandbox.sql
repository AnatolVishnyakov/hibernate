-- Откроем два терминала, в каждом из них подключимся к только что созданной базе данных sandbox.
-- Чтобы не путаться, дадим им имена. Пусть это будут Алиса и Боб.
-- Изменить подсказку командной строки можно с помощью команды \set:

\set PROMPT1 '[Alice] %/> '

-------------------------
-- Блокирующие запросы --
-------------------------

-- [Alice]
begin;
select *
from toys;

-- [Bob]
begin;
select *
from toys;

-- Таким образом параллельное выполнение двух операторов select не мешает работе каждого из них.
-- Именно такого поведения мы ожидаем от надёжной и высокопроизводительной базы данных.

-- PG_LOCK
-- Однако, транзакции Алисы и Боба до сих пор открыты.
-- Чтобы посмотреть какие блокировки были установлены, откроем третий терминал и назовём его Ева:

-- [1] diagnostic.sql

-- [Alice]
select *
from toys;
update toys set usage = usage + 1 where id = 1;

-- [Bob]
select *
from toys;
update toys set usage = usage + 1 where id = 1;

-- [2] diagnostic.sql
-- [3] diagnostic.sql

-- Если бы Алиса решила откатить или зафиксировать свою транзакцию,
-- блокировка ExclusiveLock была бы снята и Боб получил бы ShareLock.
-- [Alice]
select *
from toys;
rollback;

-- [Bob]
select *
from toys;
commit;

-- [Eva]
select * from toys;

----------------------
-- Явные блокировки --
----------------------

-- [Alice]
begin;
lock table toys in access exclusive mode;

-- [Bob]
begin;
update toys set usage = usage + 1 where id = 2;

-- В этой ситуации, запрос для отображения блокировок который мы использовали ранее, нам не поможет,
-- т. к. он использует объединение по transactionid.

-- [4] diagnostic.sql
-- [5] diagnostic.sql
-- [Alice]
commit;

-- [Bob]
select * from toys;
commit;

----------------------
-- RowExclusiveLock --
----------------------

-- [Alice]
begin;
select * from toys for update;

-- [Bob]
begin;
update toys set usage = usage + 1 where id = 3;

-- [Eva]
select * from lock_monitor;