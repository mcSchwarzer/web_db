#!/bin/bash

docker pull nshou/elasticsearch-kibana:latest
docker run -d --rm -p 9200:9200 -p 5601:5601 nshou/elasticsearch-kibana

printf "wait for elasticserver to start (Please wait about 30 Seconds)\n"
sleep 30s

#Drogendaten:
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
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/drug_poisining_mortality_usa/doc/_bulk?pretty" --data-binary @src/ndjson/drug_poisining_mortality_usandjson.json


#Korrelierte Drogendaten
curl -X PUT 'localhost:9200/correlation_drugs' -H 'Content-Type: application/json' -d'
{
    "mappings" : {
      "doc": {
        "properties" : {
            "State" : { "type" : "keyword" },
            "Correlation-Coefficient" : { "type" : "double" },
            "location" : { "type" : "geo_point" }
        }
      }
    }
}
'
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/correlation_drugs/doc/_bulk?pretty" --data-binary @src/ndjson/correlation_Drug_Poisining_mortality-UFO-Sightingsndjson.json


#UFO-Daten
curl -X PUT 'localhost:9200/ufo_data_refined' -H 'Content-Type: application/json' -d'
{
    "mappings" : {
      "doc": {
        "properties" : {
            "datetime" : { "type" : "date",  "format": "MM/dd/yyyy HH:mm"},
            "city" : { "type" : "keyword"},
            "state" : { "type" : "keyword" },
            "country" : { "type" : "keyword" },
            "shape" : { "type" : "keyword" },
            "duration (seconds)" : { "type" : "double" },
            "duration (hours/min)" : { "type" : "text" },
            "comments" : { "type" : "text" },
            "date posted" : { "type" : "date", "format": "MM/dd/yyyy"},
            "location" : { "type" : "geo_point"}
        }
      }
    }
}
'
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/ufo_data_refined/doc/_bulk?pretty" --data-binary @src/ndjson/fulldatandjson.json

#Education Daten
curl -X PUT 'localhost:9200/sichtungen_edu' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "doc":{
      "properties": {
        "Staat":{"type": "keyword"},
        "Bevoelkerung":{"type": "double"},
        "Ufo-Sichtungen":{"type": "double"},
        "Ufo-Sichtungen pro 10.000 Einwohner":{"type": "double"},
        "Durchschnittlicher SAT-Score":{"type": "double"},
        "Durchschnittlicher ACT-Score":{"type": "double"}
      }
    }
  }
}
'
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/ufo_data_refined/doc/_bulk?pretty" --data-binary @src/ndjson/sichtungen_bildung.json

#Wirtschaft Daten
curl -X PUT 'localhost:9200/corell_ufo_sandp' -H 'Content-Type: application/json' -d'
{
   "mappings" : {
     "doc": {
       "properties" : {
           "average_month": { "type": "double"},
           "ufo_sightings_count": { "type": "integer" },
           "month": {"type": "date", "format": "yyyy/MM"}
       }
     }
   }
}
'
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/corell_ufo_sandp/doc/_bulk?pretty" --data-binary @src/ndjson/sundp500ndjson.json

#FERTIG
printf "Es wurden alle Daten von ElasticSearch eingelesen und können nun in Kibana visualisiert werden\n"
