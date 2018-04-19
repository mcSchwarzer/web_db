import numpy as numpy
import json
from pprint import pprint
import datetime
import time
import math

fulldates = [1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007,
     2008, 2009, 2010, 2011, 2012, 2013, 2014]

data = json.load(open('drug_poisining_mortality_usa.json'))
vorlagedeaths = json.load(open('auswertungsvorlage.json'))
cnt = 0
for i in data:
    year = i["Year"]
    if int(year) in fulldates:
        if str(i['State']) != 'United States':
            indx = fulldates.index(int(year))
            wertarr = vorlagedeaths[i['State']]
            newval = i['Deaths']
            wertarr.pop(indx)
            wertarr.insert(indx, newval)
            vorlagedeaths[i['State']] = wertarr
            cnt += 1
            print(cnt)

with open('deathsauswertung.json', 'w') as outfile:
    json.dump(vorlagedeaths, outfile)

ufos = json.load(open('/Users/Max/Desktop/Dokumente/Theoriephase IV/Webbasierte Datenbanken/Java-Workspace/Json-Converter/UFO-Sightings_fullStatenames.json'))

l = [1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007,
     2008, 2009, 2010, 2011, 2012, 2013, 2014]
x = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]


auswertung = json.load(open(
    'auswertungsvorlage.json'))

lul = 0
wrongcounter = 0
for j in ufos:
    # print(j['fullState'])
    state = str(j["fullState"])
    if (state == "" or state == "None" or j["country"] != 'us'):
        print("wrong state" + str(wrongcounter))
    else:
        #print(state)
        temparray = auswertung[state]
        # print(j['datetime'])
        struct_time = time.strptime(str(j["datetime"]), '%m/%d/%Y %H:%M')
        year = time.strftime('%Y', struct_time)
        if int(year) in l:
            #print(year)
            idx = l.index(int(year))
            newvalue = temparray[idx] + 1
            temparray.pop(idx)
            temparray.insert(idx, newvalue)
            auswertung[state] = temparray
            lul = lul + 1
            print("DATA: " + str(lul))


with open('ufoauswertung.json', 'w') as outfile:
    json.dump(auswertung, outfile)

auswertung = json.load(open('ufoauswertung.json'))
vorlagedeaths = json.load(open('deathsauswertung.json'))

statelist = json.load(open('stateabbrev Kopie.json'))
enddatei = []
for g in statelist:
    name = str(g)
    deathsarr = vorlagedeaths[name]
    ufoarr = auswertung[name]
    print('Staat: ' + name)
    cor = numpy.corrcoef(deathsarr, ufoarr)
    if math.isnan(cor[0][1]):
        cor[0][1] = 0
    enddatei.append("\"" + name + "\": " + str(cor[0][1])) 



allstates = json.load(open('stateabbrev Kopie.json'))
usaindexdeaths = [0] * 16
usaindexufos = [0] * 16
for l in allstates:
    deathscnt = vorlagedeaths[l]
    ufoscnt = auswertung[l]
    for k in range(16):
        usaindexdeaths[k] = usaindexdeaths[k] + deathscnt[k]
        usaindexufos[k] = usaindexufos[k] + ufoscnt[k]

usacoefficient = numpy.corrcoef(usaindexdeaths, usaindexufos)[0][1]

enddatei.append('\"USA\": ' + str(usacoefficient))

with open('correlation_Drug_Poisining_mortality-UFO-Sightings.txt', 'w') as outfile:
    for i in enddatei:
        outfile.write(str(i)+"\n")


with open('correlation_Drug_Poisining_mortality-UFO-Sightings.json', 'w') as outfile:
    outfile.write("{ \n")
    for i in enddatei:
        if i != len(enddatei):
            outfile.write(str(i)+",\n")
        else:
            outfile.write(str(i)+"\n")
    outfile.write("}")

