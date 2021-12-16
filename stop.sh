#!/usr/bin/env bash

docker stop search-engine-api search-engine-logstash search-engine-mysql search-engine-kibana search-engine-elasticsearch

docker-compose down
