#!/bin/sh

./build.sh

export SPRING_APPNAME=${PWD##*/}
export SPRING_DB_TYPE=postgresql
export SPRING_DB_HOST=vinr-interactionmgr-db
export SPRING_DB_PORT=5433
export SPRING_DATABASE=vinr_interactions
export DB_USER="${USER}"
export DB_PASSWORD=password

/opt/openjdk-bin-17/bin/java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5001 -jar target/${PWD##*/}-0.0.1-SNAPSHOT.jar
