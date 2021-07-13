-- Working with PostgreSQL transactions
select now(), now();

begin;

select now(), now();

commit;