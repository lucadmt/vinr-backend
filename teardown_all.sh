#!/bin/sh

CONTAINERS=$(docker container ls -a | tail -n+2 | awk '{print $1}')
for container in $CONTAINERS
do
  docker stop "$container"
  docker rm "$container"
  echo "Stopped: $container"
done