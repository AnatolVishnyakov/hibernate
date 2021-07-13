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