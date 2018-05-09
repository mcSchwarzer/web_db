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

enddatei = []

print("Koeffizient SAT zu Ufo-Sichtungen mit allen Werten: ")
print(np.corrcoef(satlist,sightlist))
cor1 = np.corrcoef(satlist,sightlist)
cor11 = str(cor1)
enddatei.append("Koeffizient SAT zu Ufo-Sichtungen mit allen Werten: " + "\n" + cor11)

print("Koeffizient ACT zu Ufo-Sichtungen mit allen Werten: ")
print(np.corrcoef(actlist,sightlist))
cor2 = np.corrcoef(actlist, sightlist)
cor22 = str(cor2)
enddatei.append("Koeffizient ACT zu Ufo-Sichtungen mit allen Werten: " + "\n" + cor22)

#alaska entfernen
sightlist.remove(0.0)   
satlist.remove(757)
actlist.remove(16)

print("Koeffizient SAT zu Ufo-Sichtungen ohne Alaska (aufgrund fehlender Sichtungen): ")
print(np.corrcoef(satlist,sightlist))
cor3 = np.corrcoef(satlist, sightlist)
cor33 = str(cor3)
enddatei.append("Koeffizient SAT zu Ufo-Sichtungen ohne Alaska (aufgrund fehlender Sichtungen): " + "\n" + cor33)

print("Koeffizient ACT zu Ufo-Sichtungen ohne Alaska (aufgrund fehlender Sichtungen): ")
print(np.corrcoef(actlist,sightlist))
cor4 = np.corrcoef(actlist, sightlist)
cor44 = str(cor4)
enddatei.append("Koeffizient ACT zu Ufo-Sichtungen ohne Alaska (aufgrund fehlender Sichtungen): " + "\n" + cor44)


#hawaii entfernen
sightlist.remove(0.007841944)
satlist.remove(991)
actlist.remove(21)

print("Koeffizient SAT zu Ufo-Sichtungen ohne Alaska und Hawaii (aufgrund fehlender, sehr geringer Anzahl an Sichtungen): ")
print(np.corrcoef(satlist,sightlist))
cor5 = np.corrcoef(satlist, sightlist)
cor55 = str(cor5)
enddatei.append("Koeffizient SAT zu Ufo-Sichtungen ohne Alaska und Hawaii (aufgrund fehlender, sehr geringer Anzahl an Sichtungen): " + "\n" + cor55)

print("Koeffizient ACT zu Ufo-Sichtungen ohne Alaska und Hawaii (aufgrund fehlender, sehr geringer Anzahl an Sichtungen): ")
print(np.corrcoef(actlist,sightlist))
cor6 = np.corrcoef(actlist, sightlist)
cor66=str(cor6)
enddatei.append("Koeffizient ACT zu Ufo-Sichtungen ohne Alaska und Hawaii (aufgrund fehlender, sehr geringer Anzahl an Sichtungen): " + "\n" + cor66)


with open('C:\\Users\\marti\\Desktop\\DB-Project\\Ergebnisse_Korrelation_Edu', 'w') as outfile:
      for i in enddatei:
        outfile.write(str(i)+"\n")
