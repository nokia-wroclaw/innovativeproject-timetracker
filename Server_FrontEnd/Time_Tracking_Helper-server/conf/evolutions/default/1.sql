# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table time (
  id                            integer not null,
  login                         varchar(255),
  begin                         varchar(255),
  end                           varchar(255),
  constraint pk_time primary key (id)
);
create sequence time_seq;

create table tracking (
  login                         varchar(255),
  password                      varchar(255),
  date                          varchar(255),
  state                         varchar(255)
);

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

drop table if exists time;
drop sequence if exists time_seq;

drop table if exists tracking;

drop table if exists user;
drop sequence if exists user_seq;

