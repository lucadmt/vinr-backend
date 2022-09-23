#!/bin/sh

docker stop "${PWD##*/}"_app
docker rm ${PWD##*/}_app

./build.sh

DBS=$(docker container ls | tr -s ' ' | cut -f12 -d ' ' | grep "${PWD##*/}"_db)

if [ "$DBS" == "${PWD##*/}" ]; then
  docker stop "${PWD##*/}"_db
  docker rm "${PWD##*/}"_db
fi

cd ../
docker-compose up -d
