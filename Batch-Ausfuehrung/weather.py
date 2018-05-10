import numpy as np
from numpy import corrcoef as cc
from numpy import genfromtxt
import csv

print("import's are done ...")

ufolist = []
donnerlist = []
schneeEislist = []
twlist = []
hlist = []
nlist = []
nrlist = []

with open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\result.csv') as csvfile, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_d.csv') as csvfile2, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_se.csv') as csvfile3, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_tw.csv') as csvfile4, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_h.csv') as csvfile5, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_n.csv') as csvfile6, open('C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\prep_for_correl_nr.csv') as csvfile7:
    reader = csv.DictReader(csvfile)
    for row in reader:
        ufolist.append(int(row['sightings_per_day']))
    reader2 = csv.DictReader(csvfile2)
    for row in reader2:
        donnerlist.append(int(row['occurences']))
    reader3 = csv.DictReader(csvfile3)
    for row in reader3:
        schneeEislist.append(int(row['occurences']))
    reader4 = csv.DictReader(csvfile4)
    for row in reader4:
        twlist.append(int(row['occurences']))
    reader5 = csv.DictReader(csvfile5)
    for row in reader5:
        hlist.append(int(row['occurences']))
    reader6 = csv.DictReader(csvfile6)
    for row in reader6:
        nlist.append(int(row['occurences']))
    reader7 = csv.DictReader(csvfile7)
    for row in reader7:
        nrlist.append(int(row['occurences']))



s = "Donnerkorrelation: \r\n"
s += str(np.corrcoef(donnerlist,ufolist)) + "\r\n" + "\r\n"

s += "SchneeEiskorrelation: \r\n"
s = s + str(np.corrcoef(schneeEislist,ufolist)) + "\r\n" + "\r\n"

s += "tornadoWolkenkorrelation: \r\n"
s = s + str(np.corrcoef(twlist,ufolist)) + "\r\n" + "\r\n"

s += "hailkorrelation: \r\n"
s = s + str(np.corrcoef(hlist,ufolist)) + "\r\n" + "\r\n"

s += "nebelkorrelation: \r\n"
s = s + str(np.corrcoef(nlist,ufolist)) + "\r\n" + "\r\n"

s += "nrkorrelation: \r\n"
s = s + str(np.corrcoef(nrlist,ufolist)) + "\r\n" + "\r\n"





#correlateGroupedShitFml


groupedWeListN = []
groupedWeListNR = []
groupedWeListSE = []
groupedWeListH = []
groupedWeListD = []
groupedWeListTW = []

groupedUfoList = []


with open('C:\\Users\\marku\\VS\\webdb\\groupedUFOquarter.csv') as groupedSightings, open('C:\\Users\\marku\\VS\webdb\\iHateMyLife.csv') as groupedWeather:
    groupedReaderSightings = csv.DictReader(groupedSightings)
    groupedReaderWeather = csv.DictReader(groupedWeather)
    for row in groupedReaderWeather:
        groupedWeListN.append(int(row['n']))
        groupedWeListNR.append(int(row['nr']))
        groupedWeListSE.append(int(row['se']))
        groupedWeListH.append(int(row['h']))
        groupedWeListD.append(int(row['d']))
        groupedWeListTW.append(int(row['tw']))
    for row in groupedReaderSightings:
        groupedUfoList.append(int(row['sightings']))


s += "per quarter\r\n"
s +="Nebelkorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListN,groupedUfoList)) + "\r\n" + "\r\n"

s +="NieselRegenkorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListNR,groupedUfoList)) + "\r\n" + "\r\n"

s +="SchneeEiskorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListSE,groupedUfoList)) + "\r\n" + "\r\n"

s += "hagelkorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListH,groupedUfoList)) + "\r\n"+ "\r\n"

s +="donnerkorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListD,groupedUfoList)) + "\r\n"+ "\r\n"

s +="Tornadokorrelation: \r\n"
s = s + str(np.corrcoef(groupedWeListTW,groupedUfoList)) + "\r\n" + "\r\n"


print(s)

f = open('docs\\weather_Korellationen.txt', 'w+')
f.write("per day\r\")
f.write(s)
f.close()

















