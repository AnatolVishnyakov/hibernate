-- Откроем два терминала, в каждом из них подключимся к только что созданной базе данных sandbox.
-- Чтобы не путаться, дадим им имена. Пусть это будут Алиса и Боб.
-- Изменить подсказку командной строки можно с помощью команды \set:

\set PROMPT1 '[Alice] %/> '

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

-- [Eva]
\set PROMPT1 '[Eve] %/> '

select lock.locktype,
       lock.relation::regclass,
       lock.mode,
       lock.transactionid      as tid,
       lock.virtualtransaction as vtid,
       lock.pid,
       lock.granted
from pg_catalog.pg_locks lock
         left join pg_catalog.pg_database db
                   on db.oid = lock.database
where (db.datname = 'sandbox' or db.datname is null)
  and not lock.pid = pg_backend_pid() -- кроме текущей секции
order by lock.pid;

-- [Alice]
update toys set usage = usage + 1 where id = 1;

-- [Bob]
update toys set usage = usage + 1 where id = 1;

-- [Eva]
-- pg_stat_activity ещё одно интересное представление (view) из pg_catalog’а.
-- Оно показывает запросы выполняющиеся в данный момент:
select query, state, wait_event_type, wait_event, pid
from pg_stat_activity
where not (state = 'idle' or pid = pg_backend_pid());

-- [Eva]
-- Чтобы увидеть кто кого заблокировал, объединим два запроса в один:
select
    bda.pid as blocked_pid,
    bda.query as blocked_query,
    bga.pid as blocking_pid,
    bga.query as blocking_query,
    bga.datname,
    bdl.granted
from pg_catalog.pg_locks bdl
         join pg_stat_activity bda
              on bda.pid = bdl.pid
         join pg_catalog.pg_locks bgl
              on bgl.pid != bdl.pid
                  and bgl.transactionid = bdl.transactionid
         join pg_stat_activity bga
              on bga.pid = bgl.pid
where not bdl.granted;

-- [Alice]
rollback;

-- [Bob]
commit;

select * from toys;