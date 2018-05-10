curl -X PUT 'localhost:9200/weather_quarter' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "doc": {
      "properties": {
        "sn": {
          "type": "integer"
        },
        "date": {
          "type": "date",
          "format": "yyyy/MM/dd"
        },
        "n": {
          "type": "integer"
        },
        "nr": {
          "type": "integer"
        },
        "se": {
          "type": "integer"
        },
        "h": {
          "type": "integer"
        },
        "d": {
          "type": "integer"
        },
        "tw": {
          "type": "integer"
        },
        "geop": {
          "type": "geo_point"
        }
      }
    }
  }
}
'

curl -X PUT 'localhost:9200/weather_daily' -H 'Content-Type: application/json' -d'
{
 "mappings": {
   "doc": {
     "properties": {
       "Date": {
         "type": "date",
         "format": "yyyy/MM/dd"
       },
       "occurrences": {
         "type": "integer"
       },
       "type": {
         "type": "integer"
       },
       "ufo_amount": {
         "type": "integer"
       }
     }
   }
 }
}
'