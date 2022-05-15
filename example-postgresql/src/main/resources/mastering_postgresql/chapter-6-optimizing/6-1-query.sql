
select pg_sleep(10000000);

drop index idx_numbers_100;
drop index idx_numbers_1_000;
drop index idx_numbers_10_000;
drop index idx_numbers_100_000;
drop index idx_numbers_1_000_000;
drop index idx_numbers_10_000_000;
drop index idx_numbers_100_000_000;

-- 100
create table numbers_100
(
    id int
);
insert into numbers_100(id)
select *
from generate_series(1, 100);
create index idx_numbers_100 on numbers_100 (id);

-- 1_000
create table numbers_1_000
(
    id int
);
insert into numbers_1_000(id)
select *
from generate_series(1, 1000);
create index idx_numbers_1_000 on numbers_1_000 (id);

-- 10_000
create table numbers_10_000
(
    id int
);
insert into numbers_10_000(id)
select *
from generate_series(1, 10000);
create index idx_numbers_10_000 on numbers_10_000 (id);

-- 100_000
create table numbers_100_000
(
    id int
);
insert into numbers_100_000(id)
select *
from generate_series(1, 100000);
create index idx_numbers_100_000 on numbers_100_000 (id);

-- 1_000_000
create table numbers_1_000_000
(
    id int
);
insert into numbers_1_000_000(id)
select *
from generate_series(1, 1000000);
create index idx_numbers_1_000_000 on numbers_1_000_000 (id);

-- 10_000_000
create table numbers_10_000_000
(
    id int
);
insert into numbers_10_000_000(id)
select *
from generate_series(1, 10000000);
create index idx_numbers_10_000_000 on numbers_10_000_000 (id);

-- 100_000_000
create table numbers_100_000_000
(
    id int
);
insert into numbers_100_000_000(id)
select *
from generate_series(1, 100000000);
create index idx_numbers_100_000_000 on numbers_100_000_000 (id);

-- create table numbers_1_000_000_000 (id int);
-- insert into numbers_1_000_000_000(id)
-- select * from generate_series(1, 1000000000);

-- ANALYSE
explain analyse
select *
from numbers_100
where id = -1;
-- 0.05 / 0.03

explain analyse
select *
from numbers_1_000
where id = -1;
-- Seq scan: 0.229 / 0.184
-- Index only scan: 0.108 / 0.041

explain analyse
select *
from numbers_10_000
where id = -1;
-- 0.057 / 0.828
-- 0.073 / 0.025

explain analyse
select *
from numbers_100_000
where id = -1;
-- 0.08 / 8.456
-- 0.076 / 0.027

explain analyse
select *
from numbers_1_000_000
where id = -1;
-- 0.250 / 97.388
-- 0.090 / 0.033

explain analyse
select *
from numbers_10_000_000
where id = -1;
-- 0.252 / 555.385
-- 0.075 / 0.026

explain analyse
select *
from numbers_100_000_000
where id = -1;
-- 0.059 / 4163.255
-- 0.088 / 0.032

-- .\pg_ctl.exe restart -D "D:\Program Files (x86)\PostgreSQL\10\data"
-- "D:\Program Files (x86)\PostgreSQL\10\bin\pg_ctl.exe" restart -D "D:\Program Files (x86)\PostgreSQL\10\data"
-- create extension pg_stat_statements;
-- select pg_stat_reset();
-- select pg_stat_statements_reset();
select * from pg_stat_user_tables;

select * from pg_stat_user_indexes;
select * from pg_stat_statements
-- where query = 'select *
-- from numbers_100_000_000
-- where id = $1'
where calls > 3
order by total_time desc;
