FROM java:8-jre

COPY ./target/*.jar /data/target/

CMD java -jar /data/target/es-events-indexer-1.0-SNAPSHOT.jar server