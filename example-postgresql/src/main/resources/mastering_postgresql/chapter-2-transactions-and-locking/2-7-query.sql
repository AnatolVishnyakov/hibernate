-- Considering alternative solutions
-- (альтернативные варианты)

-- Есть альтернативное решение проблемы.
-- Рассмотрим следующий пример: вас просят написать приложение,
-- генерирующее номера счетов. Налоговая инспекция может потребовать
-- от вас создать номера счетов без пробелов и дубликатов.
-- Как бы ты это сделал? Конечно, одним из решений была бы
-- блокировка таблицы. Однако есть вариант лучше:

create table t_invoice (id int PRIMARY KEY);
create table t_watermark (id int);
insert into t_watermark values (0);
with x as (update t_watermark set id = id + 1 returning *)
    insert into t_invoice
    select * from x returning *;

-- В этом случае создается таблица t_watermark.
-- Она содержит всего одну строку. Сначала будет выполнено WITH.
-- Строка будет заблокирована и увеличена на единицу,
-- и будет возвращено новое значение.
-- Только один человек может делать это одновременно.
-- Возвращаемое значение, затем используется в таблице t_invoice.
-- Гарантия уникальности.
-- Прелесть в том, что в таблице t_watermark используется
-- только блокировка строки; чтение в таблице t_invoice не будет заблокировано.
-- В целом этот способ более масштабируемый.