FROM mysql:8
MAINTAINER tmdgus0118@gmail.com


COPY rest_service.sql /docker-entrypoint-initdb.d/initial.sql
ADD ./custom.cnf /etc/mysql/conf.d/custom.cnf
