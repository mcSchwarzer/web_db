#!/bin/bash

docker pull nshou/elasticsearch-kibana:latest
docker run -d -p 9200:9200 -p 5601:5601 nshou/elasticsearch-kibana