-- Understanding transaction isolation levels

drop table if exists t_account;
create table if not exists t_account (id int primary key, balance int);
begin;
insert into t_account select id, 100 from generate_series(1, 20) AS id;
commit;

select * from t_account;
rollback;

-- Transaction 1                            Transaction 2
BEGIN;
SELECT sum(balance) FROM t_account;
-- User will see 300
                                         BEGIN;
                                         INSERT INTO t_account (id, balance) VALUES ((select max(id) + 1 from t_account), 100);
                                         COMMIT;
SELECT sum(balance) FROM t_account;
-- User will see 400
COMMIT;

-- Большинство пользователей на самом деле ожидают,
-- что левая транзакция всегда будет возвращать 300
-- независимо от второй транзакции. Однако это не так.
-- По умолчанию PostgreSQL работает в режиме изоляции
-- транзакции READ COMMITTED. Это означает, что каждый
-- оператор внутри транзакции получит новый снимок данных,
-- который будет постоянным на протяжении всего запроса.

-- Если вы хотите избежать этого, вы можете использовать
-- TRANSACTION ISOLATION LEVEL REPEATABLE READ.
-- На этом уровне изоляции транзакций будет использоваться
-- один и тот же снимок для всей транзакции.
-- Вот что произойдет:

-- Transaction 1                                            Transaction 2
BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ;
SELECT sum(balance) FROM t_account;
-- User will see 300
                                                            BEGIN;
                                                            INSERT INTO t_account (id, balance) VALUES ((select max(id) + 1 from t_account), 100);
                                                            COMMIT;
SELECT sum(balance) FROM t_account;                         SELECT sum(balance) FROM t_account;
-- User will see 300                                        User will see 400
COMMIT;

-- полезно при использовании в отчетах
-- repeatable read не дороже read committed