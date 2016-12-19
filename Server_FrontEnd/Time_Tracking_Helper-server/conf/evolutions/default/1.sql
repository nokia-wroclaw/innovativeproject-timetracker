# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table schedule (
  id                            integer not null,
  login                         varchar(255),
  day                           varchar(255),
  begin                         time,
  end                           time,
  constraint pk_schedule primary key (id)
);
create sequence schedule_seq;

create table time (
  id                            integer not null,
  login                         varchar(255),
  begin                         timestamp,
  end                           timestamp,
  constraint pk_time primary key (id)
);
create sequence time_seq;

create table user (
  id                            integer not null,
  login                         varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  email                         varchar(255),
  constraint pk_user primary key (id)
);
create sequence user_seq;


# --- !Downs

drop table if exists schedule;
drop sequence if exists schedule_seq;

drop table if exists time;
drop sequence if exists time_seq;

drop table if exists user;
drop sequence if exists user_seq;

