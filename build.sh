#!/bin/sh

rm -rf ./target
mvn install
mvn compile
mvn package
docker build . -t vinr_"${PWD##*/}":latest
