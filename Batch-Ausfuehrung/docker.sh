#!/bin/bash

docker pull nshou/elasticsearch-kibana:latest
docker run -d --rm -p 9200:9200 -p 5601:5601 nshou/elasticsearch-kibana

printf "wait for elasticserver to start\n"
sleep 30s

curl -X PUT 'localhost:9200/drug_poisining_mortality_usa' -H 'Content-Type: application/json' -d'
{
    "mappings" : {
      "doc": {
        "properties" : {
            "State" : { "type" : "keyword"},
            "Year" : { "type" : "date", "format": "yyyy"},
            "Sex" : { "type" : "keyword" },
            "Age Group" : { "type" : "keyword" },
            "Race and Hispanic Origin" : { "type" : "keyword" },
            "Deaths" : { "type" : "integer" },
            "Population" : { "type" : "integer" },
            "Crude Death Rate" : { "type" : "double" },
            "Standard Error for Crude Rate" : { "type" : "double"},
            "Lower Confidence Limit for Crude Rate" : { "type" : "double" },
            "Upper Confidence Limit for Crude Rate" : { "type" : "double" },
            "Age-adjusted Rate" : { "type" : "double" },
            "Standard Error for Age-adjusted Rate" : { "type" : "double" },
            "Lower Confidence Limit for Age-adjusted Rate" : { "type" : "double" },
            "Upper Confidence Limit for Age-adjusted Rate" : { "type" : "double" },
            "State Crude Rate in Range" : { "type" : "text" },
            "US Crude Rate" : { "type" : "double" },
            "US Age-Adjusted Rate" : { "type" : "double" }
        }
      }
    }
}
'
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/drug_poisining_mortality_usa/doc/_bulk?pretty" --data-binary @src/drug_poisining_mortality_usandjson.json

