# Elasticsearch und Kibana
Elasticsearch und Kibana helfen zur Visualisierung und letztlich Datenauswertung.
Um die relevanten Daten einzuspeisen folgen Sie bitte folgenden Anweisungen:

### 1. Benötigte Datensätze
- Ufo-Daten: **TODO ND JSON INS DRIVE**
- Drogendaten: https://drive.google.com/open?id=1oESnEwWEJAHBBr0tgEvg3k82QeRxPZ3O
- Drogenkorrelation: https://drive.google.com/open?id=1nsj_6KIEBnxW7o3PJTlgTvzxG6OE7gR1
- Educationdaten: https://drive.google.com/open?id=1A5JGewPjx2ybdR4kjknxiqlf0G0OkMOM


### 2. Auf der Kibana Devtools-Konsole den Index initialisieren:
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
```python
PUT /drug_poisining_mortality_usa
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

```
- Drogenkorrelation:
```javascript
PUT /correlation_drugs
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

### 3. Einfügen der fertigen ND-JSON-Datensätze über Curl
Dieser Befehl muss über die Bash aus dem Curl-Ordner ausgeführt werden *(/bin)*.
- UFO-Daten:
```javascript
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/ufo_data_refined/doc/_bulk?pretty" --data-binary @PFAD_ZUM_UFO_DATENSATZ_IN_NDJSON
```
- Drogen-Daten:
```python
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/drug_poisining_mortality_usa/doc/_bulk?pretty" --data-binary @PFAD_ZUM_DROGEN_DATENSATZ_IN_NDJSON
```
- Drogenkorrelation:
```javascript
curl -H "Content-Type: application/x-ndjson" –XPUT "http://localhost:9200/correlation_drugs/doc/_bulk?pretty" --data-binary @PFAD_ZUM_DROGEN_DATENSATZ-DROGEN-KORRELATION_IN_NDJSON
```
- Education-Daten:
```javascript
curl -H "Content-Type: application/x-ndjson" -XPUT "http://localhost:9200/sichtungen_edu/doc/_bulk?pretty" --data-binary @PFAD_ZUM_EDU_DATENSATZ_IN_NDJSON
```
