[![Circle CI](https://circleci.com/gh/gaia-adm/es-events-indexer.svg?style=svg)](https://circleci.com/gh/gaia-adm/es-events-indexer) [![Codacy Badge](https://api.codacy.com/project/badge/grade/ce3927e9fbe74372b3778d4debc663ad)](https://www.codacy.com/app/alexei-led/es-events-indexer) [![](https://badge.imagelayers.io/gaiaadm/es-event-indexer:latest.svg)](https://imagelayers.io/?images=gaiaadm/es-event-indexer:latest 'Get your own badge on imagelayers.io')

Preface
=======
This service reads data from Rabbitmq queue named __es-events-indexer__ and writes the data into ElasticSearch.

It works with ElasticSearch 2.0 bulk API (expecting events from RabbitMQ in the bulk format)
Check out the testing sections in circle.yml in to root directory, to see how you can use this service.
