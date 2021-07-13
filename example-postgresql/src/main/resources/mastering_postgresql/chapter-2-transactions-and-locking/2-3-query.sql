-- Making use of SAVEPOINT
begin;
select 1;
savepoint a;
select 2 / 0;
select 2;
rollback to savepoint a;
select 3;
commit;