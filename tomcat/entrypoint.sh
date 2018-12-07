#!/bin/bash

echo "DB Server Wait"
dockerize -wait tcp://mysql_rest_service:3306 -timeout 40s

echo "TOMCAT START $CATALINA_HOME" 
catalina.sh run