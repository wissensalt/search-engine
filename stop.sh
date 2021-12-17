#!/usr/bin/env bash

docker stop search-engine-api search-engine-logstash search-engine-mysql search-engine-kibana search-engine-elasticsearch search-engine-ui

docker-compose down
