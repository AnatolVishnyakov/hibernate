-- 100
create table if not exists numbers_100
(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_100 on numbers_100 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_100 on numbers_100 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_100 on numbers_100 using btree(date_field_idx_btree);

-- 1_000
create table if not exists numbers_1_000
(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_1_000 on numbers_1_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_1_000 on numbers_1_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_1_000 on numbers_1_000 using btree(date_field_idx_btree);

-- 10_000
create table if not exists numbers_10_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_10_000 on numbers_10_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_10_000 on numbers_10_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_10_000 on numbers_10_000 using btree(date_field_idx_btree);

-- 100_000
create table if not exists numbers_100_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_100_000 on numbers_100_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_100_000 on numbers_100_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_100_000 on numbers_100_000 using btree(date_field_idx_btree);

-- 1_000_000
create table if not exists numbers_1_000_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_1_000_000 on numbers_1_000_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_1_000_000 on numbers_1_000_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_1_000_000 on numbers_1_000_000 using btree(date_field_idx_btree);

-- 10_000_000
create table if not exists numbers_10_000_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_10_000_000 on numbers_10_000_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_10_000_000 on numbers_10_000_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_10_000_000 on numbers_10_000_000 using btree(date_field_idx_btree);

-- 100_000_000
create table if not exists numbers_100_000_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_100_000_000 on numbers_100_000_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_100_000_000 on numbers_100_000_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_100_000_000 on numbers_100_000_000 using btree(date_field_idx_btree);

-- 1_000_000_000
create table if not exists numbers_1_000_000_000(
    id bigserial not null primary key,
    number_field int,
    number_field_idx_btree int,
    text_field text,
    text_field_idx_btree text,
    date_field time with time zone,
    date_field_idx_btree time with time zone
);
create index if not exists idx_number_field_idx_1_000_000_000 on numbers_1_000_000_000 using btree(number_field_idx_btree);
create index if not exists idx_text_field_idx_btree_1_000_000_000 on numbers_1_000_000_000 using btree(text_field_idx_btree);
create index if not exists idx_date_field_idx_btree_1_000_000_000 on numbers_1_000_000_000 using btree(date_field_idx_btree);