CircleCI build status: [![Circle CI](https://circleci.com/gh/gaia-adm/es-events-indexer.svg?style=svg)](https://circleci.com/gh/gaia-adm/es-events-indexer)

Preface
=======
This service reads data from Rabbitmq queue named __es-events-indexer__ and writes the data into ElasticSearch.

It works with ElasticSearch 2.0 bulk API (expecting events from RabbitMQ in the bulk format)
Check out the testing sections in circle.yml in to root directory, to see how you can use this service.
