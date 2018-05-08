import json
import io

emptyCountryB = 0
emptyStateB = 0

with open('<Path to original ufo data file>') as data_file:
    data = json.load(data_file)
    for item in data:
        if item["country"] == "" or item["country"] == None or item["country"] == " ":
            emptyCountryB += 1
        if item["state"] == "" or item["state"] == None or item["state"] == " ":
            emptyStateB += 1

print("Anzahl nicht vorhandener country datensaetze davor: " + str(emptyCountryB))
print("Anzahl nicht vorhandener state datensaetze davor: " + str(emptyStateB))

emptyCountryA = 0
emptyStateA = 0

with open('<Path to edited file>') as data_file:
    data = json.load(data_file)
    for item in data:
        if item["country"] == "" or item["country"] == None or item["country"] == " ":
            emptyCountryA += 1
        if item["state"] == "" or item["state"] == None or item["state"] == " ":
            emptyStateA += 1

print("Anzahl nicht vorhandener country datensaetze danach: " + str(emptyCountryA))
print("Anzahl nicht vorhandener state datensaetze danach: " + str(emptyStateA))
