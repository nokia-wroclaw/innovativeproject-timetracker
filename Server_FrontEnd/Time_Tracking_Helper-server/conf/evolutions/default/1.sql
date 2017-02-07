# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table privileges (
  id                            integer not null,
  userfrom                      varchar(255),
  userto                        varchar(255),
  estimated_hours               integer,
  constraint pk_privileges primary key (id)
);
create sequence privileges_seq;

create table request (
  id                            integer not null,
  userfrom                      varchar(255),
  userto                        varchar(255),
  constraint pk_request primary key (id)
);
create sequence request_seq;

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
  state                         varchar(255),
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

drop table if exists privileges;
drop sequence if exists privileges_seq;

drop table if exists request;
drop sequence if exists request_seq;

drop table if exists schedule;
drop sequence if exists schedule_seq;

drop table if exists time;
drop sequence if exists time_seq;

drop table if exists user;
drop sequence if exists user_seq;

