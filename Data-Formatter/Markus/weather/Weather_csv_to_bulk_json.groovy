This groovy script transforms the weather and the weatherstations data from csv format into the NDJSON format with indexlines for elasticsearch and joins weatherdata with stationdata.

  Input: csv files with weather data from 1970 - 2015 (those are the files directly from kaggle: https://www.kaggle.com/mcblacker/first-try-to-get-weatherdata)
         csv file with the weatherstations data
         the data originally comes from National Oceanic and Atmospheric Administration (NOAA)
         
         csv format weather data: 
              stationenNummer,jahr,monat,tag,temperatur,nebel,nieselRegen,schneeEis,hagel,donner,tornadoWolke ("SELECT stn AS stationenNummer,year AS jahr,mo AS monat,da AS tag,temp AS temperatur,fog AS nebel,rain_drizzle AS nieselRegen,snow_ice_pellets AS schneeEis,hail AS hagel,thunder AS donner,tornado_funnel_cloud as tornadoWolke FROM `bigquery-public-data.noaa_gsod.gsod%d`")
         csv format station data:
              stationenNummer,latitude,longitude ("SELECT usaf AS stationenNummer, lat As latitude, lon AS longitude FROM `bigquery-public-data.noaa_gsod.stations` WHERE country = 'US' AND lat IS NOT NULL AND lon IS NOT NULL AND NOT (lat = 0.0 AND lon = 0.0) ORDER BY usaf")
              
  Output: multiple .NDJSON files with indexlines for elasticsearch 
          e.g.  {"index":{"_index":"us_weather_70_to_15","_id":"0"}}
                {"sn":"724096","date":"1970/01/01","n":"0","nr":"0","se":"0","h":"0","d":"0","tw":"0","geop": { "lat": "40.017","lon":"-74.583"}}
          the data is sliced into files with 300.000 entries each so the curl command would work in the next step.
          
          a .cmd file with all the curl commands to push the data to elasticsearch
          
          
