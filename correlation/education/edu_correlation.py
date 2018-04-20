import numpy as np
from numpy import corrcoef as cc
from numpy import genfromtxt
import csv

print("----import successful----")

sightlist = []
satlist = []
actlist = []


with open('C:\\Users\\marti\\Desktop\\DB-Project\\sichtungen_bildung.csv') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        print(row['Ufo-Sichtungen pro 10.000 Einwohner'])
        sightlist.append(float(row['Ufo-Sichtungen pro 10.000 Einwohner']))
 

with open('C:\\Users\\marti\\Desktop\\DB-Project\\sichtungen_bildung.csv') as csvfile2:
    reader2 = csv.DictReader(csvfile2)
    for row in reader2:
        print(row['Durchschnittlicher SAT-Score'])
        satlist.append(int(row['Durchschnittlicher SAT-Score']))

with open('C:\\Users\\marti\\Desktop\\DB-Project\\sichtungen_bildung.csv') as csvfile3:
    reader3 = csv.DictReader(csvfile3)
    for row in reader3:
        print(row['Durchschnittlicher ACT-Score'])
        actlist.append(int(row['Durchschnittlicher ACT-Score']))
   


print(sightlist)
print(satlist)
print(actlist)
print(sightlist.__sizeof__())
print(satlist.__sizeof__())
print(actlist.__sizeof__())



print("Koeffizient SAT zu Ufo-Sichtungen: ")
print(np.corrcoef(satlist,sightlist))
print("Koeffizient ACT zu Ufo-Sichtungen: ")
print(np.corrcoef(actlist,sightlist))
