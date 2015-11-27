#!/bin/sh

#cp Dockerfile Dockerfile.tmp
#cp Dockerfile.build Dockerfile
#docker build -t build-img .
#docker create --name build-cont build-img
#docker cp build-cont:/usr/local/gaia/target/events-indexer-1.0-SNAPSHOT.jar ./target
#
#cp Dockerfile.tmp Dockerfile
#docker build -t gaiaadm/event-indexer .
#
#docker run -d -p 5673:5672 -p 15673:15672 -e RABBITMQ_PASS="mypass" --name rabbitmq tutum/rabbitmq
#docker run -d -p 8083:8083 -p 8086:8086 -e PRE_CREATE_DB="db1" --name influxdb tutum/influxdb
#docker run -d -p 9000:8080 -p 9001:8081 --link rabbitmq:rabbitmq --name faker gaiaadm/data-faker java -Ddw.rabbitmq.username=admin -Ddw.rabbitmq.password=mypass -jar /data/target/data-faker-1.0-SNAPSHOT.jar server; sleep 10
#curl -X POST 'http://localhost:9000/fake-data?repeat=2&sendto=rabbitmq'
#docker run -d -p 8080:8080 -p 8081:8081 --link rabbitmq:rabbitmq --link influxdb:influxdb --name indexer gaiaadm/event-indexer java -Ddw.rabbitmq.username=admin -Ddw.rabbitmq.password=mypass -jar /data/target/events-indexer-1.0-SNAPSHOT.jar server; sleep 10
#curl -G 'http://localhost:8086/query?db=db1&u=root&p=root' --data-urlencode 'q=select * from my_time_series limit 1' | grep 'status' | grep '92'
