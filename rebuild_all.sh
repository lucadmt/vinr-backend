#!/bin/bash


./teardown_all.sh
for dir in {eureka,apigateway,user_mgr,interaction_mgr,notification_mgr}
do
  cd "$dir" || exit
  ./build.sh
  cd ..
done
docker-compose up -d
sleep 20
docker start user_mgr_app interaction_mgr_app notification_mgr_app
docker image prune -f
docker volume prune -f
