import numpy as np 
import pandas as pd 
import os
import bq_helper 
print(os.listdir("../input"))

# create a helper object for our bigquery dataset
DatabaseHelper = bq_helper.BigQueryHelper(active_project= "bigquery-public-data", dataset_name= "noaa_gsod")

query_List = []

for x in range(1970, 2017): query_List.append("SELECT stn AS stationenNummer,year AS jahr,mo AS monat,da AS tag,temp AS temperatur,fog AS nebel,rain_drizzle AS nieselRegen,snow_ice_pellets AS schneeEis,hail AS hagel,thunder AS donner,tornado_funnel_cloud as tornadoWolke FROM `bigquery-public-data.noaa_gsod.gsod%d`" % (x)) # select whatever you want here ...
print (query_List)

sum_of_query_sizes = 0.0
for query in query_List: sum_of_query_sizes += DatabaseHelper.estimate_query_size(query)
print (sum_of_query_sizes) #cmplx of all queries in query_list[]

year = 1970
#for every query in the query list execute the sql statement and save the resulting csv file in the output dir 
for exQuery in query_List: 
    dataframe = DatabaseHelper.query_to_pandas_safe(exQuery, max_gb_scanned=5)
    dataframe.to_csv('wetter_US_%d.csv' % (year), index = False)
    print ("saved wetter_US_%d" % (year))
    year += 1;

#stations:
stationsQuery =  """SELECT usaf AS stationenNummer, lat As latitude, lon AS longitude FROM `bigquery-public-data.noaa_gsod.stations` WHERE country = 'US' AND lat IS NOT NULL AND lon IS NOT NULL AND NOT (lat = 0.0 AND lon = 0.0) ORDER BY usaf"""
stationsComplx = DatabaseHelper.estimate_query_size(stationsQuery)
print("querySize for stations = %d" % (stationsComplx))

stations = DatabaseHelper.query_to_pandas_safe(stationsQuery, max_gb_scanned=0.1) #cmplx is not too big ... 
stations.to_csv('WetterStationen_US.csv', index = False) #saving stationsFile as output

#problems:  1. you could just save all files from 1970 - 2017 but you get an error ath like : "memory space not enough" --> you have to do it almost one at a time and save it locally
#           2. you could perform a join with dataframes (stationsNummer to lat, lon) but for that the memory from the kernel is not enough ... atleast not if you have files that big 
#                        --> do it manually with for examplw java: HashMap with station key = stationNumber object = string --> you can check with .contains() 
#           3. the stationsNumber "999999" is there wy too often ... so you can maybe just remove it completly
#           4. lat & lon is to 4% null ... also remove the stations where both lat and lon are 0.0
