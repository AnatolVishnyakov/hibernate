-- Understanding simple queries and the cost model
drop table if exists t_test;
create table t_test (id serial, name text);

insert into t_test (name) select 'hans' from generate_series(1, 2000000);

insert into t_test (name) select 'paul' from generate_series(1, 2000000);

select name, count(*) from t_test group by 1;

\timing

-- Making use of EXPLAIN
EXPLAIN SELECT * FROM t_test WHERE id = 432332;

SET max_parallel_workers_per_gather TO 0;

-- Digging into the PostgreSQL cost model
EXPLAIN SELECT * FROM t_test WHERE id = 432332;

SELECT pg_relation_size('t_test') / 8192.0; -- 21622

SHOW seq_page_cost; -- 1

SHOW cpu_tuple_cost; -- 0.01

SHOW cpu_operator_cost; -- 0.0025

SELECT 21622*1 + 4000000*0.01 + 4000000*0.0025; -- 71622

-- Deploying simple indexes
CREATE INDEX idx_id ON t_test (id); -- 86 MB

\di+

-- Making use of sorted output
EXPLAIN analyse SELECT * FROM t_test ORDER BY id DESC;

-- В этом случае индекс уже возвращает данные в
-- правильном порядке сортировки, и поэтому нет
-- необходимости сортировать весь набор данных.
-- Чтобы ответить на этот запрос, достаточно
-- будет прочитать последние 10 строк индекса.
-- Index Scan Backward using idx_id on t_test
--      (cost=0.43..125505.43 rows=4000000 width=9)

explain analyse SELECT min(id), max(id) FROM t_test;
-- В PostgreSQL индекс (точнее, b-дерево) можно
-- читать в обычном порядке или в обратном порядке.
-- Дело в том, что b-дерево можно рассматривать как
-- отсортированный список. Итак, естественно, самое
-- низкое значение находится в начале, а максимальное
-- значение - в конце. Следовательно, min и max - идеальные
-- кандидаты для ускорения. Также стоит отметить,
-- что в этом случае ссылка на основную таблицу не требуется.