-- Handling errors inside a transaction
begin;
select 1;
select 1 / 0;
select 1;
commit;
rollback;