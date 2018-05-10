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



print("Donnerkorrelation: ")
print(np.corrcoef(donnerlist,ufolist))

print("SchneeEiskorrelation: ")
print(np.corrcoef(schneeEislist,ufolist))

print("tornadoWolkenkorrelation: ")
print(np.corrcoef(twlist,ufolist))

print("hailkorrelation: ")
print(np.corrcoef(hlist,ufolist))

print("nebelkorrelation: ")
print(np.corrcoef(nlist,ufolist))

print("nrkorrelation: ")
print(np.corrcoef(nrlist,ufolist))





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


print("GROOOOOOOOOOOOOOOOOOOOOOOOUPED \n")

print("Nebelkorrelation: ")
print(np.corrcoef(groupedWeListN,groupedUfoList))

print("NieselRegenkorrelation: ")
print(np.corrcoef(groupedWeListNR,groupedUfoList))

print("SchneeEiskorrelation: ")
print(np.corrcoef(groupedWeListSE,groupedUfoList))

print("hagelkorrelation: ")
print(np.corrcoef(groupedWeListH,groupedUfoList))

print("donnerkorrelation: ")
print(np.corrcoef(groupedWeListD,groupedUfoList))

print("Tornadokorrelation: ")
print(np.corrcoef(groupedWeListTW,groupedUfoList))





















