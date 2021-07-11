create database sandbox;

drop table if exists toys;

create table toys
(
    id    serial  not null,
    name  character varying(36),
    usage integer not null default 0,
    constraint toys_pkey primary key (id)
);

insert into toys(name)
values ('car'),
       ('digger'),
       ('shovel');