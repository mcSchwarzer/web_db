# Elasticsearch und Kibana
Elasticsearch und Kibana helfen zur Visualisierung und letztlich Datenauswertung.
Um die relevanten Daten einzuspeisen folgen Sie bitte folgenden Anweisungen:

### 1. Auf der Kibana Devtools-Konsole den Index initialisieren:
- Ufo-Daten:
 ```javascript
 PUT /ufo_data_refined
{
    "mappings" : {
      "doc": {
        "properties" : {
            "date" : { "type" : "date",  "format": "MM/dd/yyyy"},
            "time" : { "type" : "date",  "format": "hour_minute"},
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
```
- Drogendaten:
```javascript
PUT /correlation_drugs
{
    "mappings" : {
      "doc": {
        "properties" : {
            "State" : { "type" : "keyword" },
            "Correlation-Coefficient" : { "type" : "double" }
        }
      }
    }
}
```
- Education-Daten:
```javascript
PUT /sichtungen_edu
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
```

### 2. Einfügen der fertigen ND-JSON-Datensätze über Curl
Dieser Befehl muss über die Bash aus dem Curl-Ordner ausgeführt werden *(/bin)*.
- UFO-Daten:
```javascript
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/ufo_data_refined/doc/_bulk?pretty" --data-binary @PFAD_ZUM_UFO_DATENSATZ_IN_NDJSON
```
- Drogen-Daten:
```javascript
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/correlation_drugs/doc/_bulk?pretty" --data-binary @PFAD_ZUM_DROGEN_DATENSATZ_IN_NDJSON
```
- Education-Daten:
```javascript
curl -H "Content-Type: application/x-ndjson" -XPUT "http://localhost:9200/sichtungen_edu/doc/_bulk?pretty" --data-binary @PFAD_ZUM_EDU_DATENSATZ_IN_NDJSON
```
