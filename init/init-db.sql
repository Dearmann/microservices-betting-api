create database if not exists betdb;
create database if not exists commentdb;
create database if not exists ratedb;
create database if not exists matchdb;

create user 'bet'@'%' identified by 'secret';
grant all privileges on betdb.* to 'bet'@'%';

create user 'comment'@'%' identified by 'secret';
grant all privileges on commentdb.* to 'comment'@'%';

create user 'rate'@'%' identified by 'secret';
grant all privileges on ratedb.* to 'rate'@'%';

create user 'match'@'%' identified by 'secret';
grant all privileges on matchdb . * to 'match'@'%';