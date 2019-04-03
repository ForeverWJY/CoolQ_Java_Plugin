CREATE DATABASE `coolq` charset UTF8;

USE `mysql`;
CREATE USER 'coolq'@'%' IDENTIFIED BY 'coolq';
GRANT all privileges ON coolq.* TO 'coolq'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;