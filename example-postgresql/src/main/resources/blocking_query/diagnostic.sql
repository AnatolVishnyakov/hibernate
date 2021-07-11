-- [Eva]
\set PROMPT1 '[Eve] %/> '

-- [Eva]
-- [1] Какие блокировки (активные) были установлены?
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

-- [Eva]
-- [2] Какие запросы выполняются в данный момент?
select query, state, wait_event_type, wait_event, pid
from pg_stat_activity
where not (state = 'idle' or pid = pg_backend_pid());

-- [Eva]
-- [3] Кто кого заблокировал?
select bda.pid   as blocked_pid,
       bda.query as blocked_query,
       bga.pid   as blocking_pid,
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

-- [Eva]
-- [4]
select pid, query, now() - query_start as waiting_duration, a.*
from pg_catalog.pg_stat_activity a;

-- [Eva]
-- [5] Сделав объединение по столбцам relation и locktype,
-- мы снова можем видеть кто кого блокирует:
select bgl.relation::regclass,
       bda.pid   as blocked_pid,
       bda.query as blocked_query,
       bdl.mode  as blocked_mode,
       bga.pid   AS blocking_pid,
       bga.query as blocking_query,
       bgl.mode  as blocking_mode
from pg_catalog.pg_locks bdl
         join pg_stat_activity bda
              on bda.pid = bdl.pid
         join pg_catalog.pg_locks bgl
              on bdl.pid != bgl.pid
                  and bgl.relation = bdl.relation
                  and bgl.locktype = bdl.locktype
         join pg_stat_activity bga
              on bga.pid = bgl.pid
where not bdl.granted;

-- [6] Кто кого заблокировал (по времени)?
select coalesce(bgl.relation::regclass::text, bgl.locktype) as locked_item,
       now() - bda.query_start                              as waiting_duration,
       bda.pid                                              as blocked_pid,
       bda.query                                            as blocked_query,
       bdl.mode                                             as blocked_mode,
       bga.pid                                              as blocking_pid,
       bga.query                                            as blocking_query,
       bgl.mode                                             as blocking_mode
from pg_catalog.pg_locks bdl
         join pg_stat_activity bda
              on bda.pid = bdl.pid
         join pg_catalog.pg_locks bgl
              on bgl.pid != bdl.pid
                  and (bgl.transactionid = bdl.transactionid
                      or bgl.relation = bdl.relation and bgl.locktype = bdl.locktype)
         join pg_stat_activity bga
              on bga.pid = bgl.pid
                  and bga.datid = bda.datid
where not bdl.granted
  and bga.datname = current_database();

-- [7] Lock monitor
create view lock_monitor as
(
select coalesce(bgl.relation::regclass::text, bgl.locktype) as locked_item,
       now() - bda.query_start                              as waiting_duration,
       bda.pid                                              as blocked_pid,
       bda.query                                            as blocked_query,
       bdl.mode                                             as blocked_mode,
       bga.pid                                              as blocking_pid,
       bga.query                                            as blocking_query,
       bgl.mode                                             as blocking_mode
from pg_catalog.pg_locks bdl
         join pg_stat_activity bda
              on bda.pid = bdl.pid
         join pg_catalog.pg_locks bgl
              on bgl.pid != bdl.pid
                  and (bgl.transactionid = bdl.transactionid
                      or bgl.relation = bdl.relation and bgl.locktype = bdl.locktype)
         join pg_stat_activity bga
              on bga.pid = bgl.pid
                  and bga.datid = bda.datid
where not bdl.granted
  and bga.datname = current_database()
    );