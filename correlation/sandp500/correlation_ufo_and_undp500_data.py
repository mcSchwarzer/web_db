import numpy as numpy
import json
from pprint import pprint
import datetime
import time

fulldates = [2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013]
s_and_p_500_per_month = [[104, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]

data = json.load(open('sundp500.json'))
sandpCnt = 0
for i in data:
    sandpCnt += 1
    struct_time = time.strptime(str(i["Date"]), '%Y-%m-%d')
    year = time.strftime('%Y', struct_time)
    month = int(float(time.strftime('%m', struct_time)))
    if int(year) in fulldates:
        indx = fulldates.index(int(year))
        if s_and_p_500_per_month[indx][month - 1] == 0:
            s_and_p_500_per_month[indx][month -1] += (i['Open'])
        else:
            s_and_p_500_per_month[indx][month -1] += i['Open']
            s_and_p_500_per_month[indx][month -1] = s_and_p_500_per_month[indx][month -1] / 2
    print(str(sandpCnt) + "/" + str(len(data)))



ufos = json.load(open('UFO Daten/Python_ufo_data.json'))

l = [2005, 2006, 2007,
     2008, 2009, 2010, 2011, 2012, 2013]
ufo_sightings_per_month = [[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]

ufoCnt = 0
wrongcounter = 0
for j in ufos:
    ufoCnt += 1
    country = str(j['country'])
    if country.lower() == 'us':
        struct_time = time.strptime(str(j['datetime']), '%m/%d/%Y %H:%M')
        year = time.strftime('%Y', struct_time)
        month = int(float(time.strftime('%m', struct_time)))
        
        if int(year) in l:
            indx = fulldates.index(int(year))
            ufo_sightings_per_month[indx][month - 1] += 1
        print("Progress ufodata: " + str(ufoCnt) + "/" + str(len(ufos)))

print("\n")
print("\n")

print(numpy.matrix(s_and_p_500_per_month))
print(numpy.matrix(ufo_sightings_per_month))
print()

print("Correlation Ufosichtungen der USA und Standard and Poor Top 500 Aktien der USA:")

results = [0, 0, 0, 0, 0, 0, 0, 0, 0]
for j in range(0, 9):
    results[j] = str(numpy.corrcoef(s_and_p_500_per_month[j], ufo_sightings_per_month[j])[0][1])
    print(str(fulldates[j])+ ": " + results[j])

print("\n")
print("Gesamtkorrelation (Matrix): ")
print(str(numpy.corrcoef(s_and_p_500_per_month, ufo_sightings_per_month)[0][1]))
