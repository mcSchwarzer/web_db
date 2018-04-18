import io
import reverse_geocoder as rg
import json

myCounter = 0
countryFoundCounter = 0
finalstring = ''

with open('/Users/Max/Desktop/Dokumente/Theoriephase IV/Webbasierte Datenbanken/Java-Workspace/Json-Converter/UFO-Sightings_fullStatename.json') as data_file:
    data = json.load(data_file)
    for item in data:
        if item["country"] == "":
            if not item["latitude"] == 0 and not item["longitude"] == 0:
                coordinates = (item["latitude"], item["longitude"])
                results = rg.search(coordinates, mode=1)
                item["country"] = list(results[0].items())[5][1].lower()
                item["fullState"] = list(results[0].items())[3][1]
                countryFoundCounter += 1
        with open("finaledata.json", 'a') as f:
            if myCounter == 0:
                f.write("[\r\n")
            json.dump(item, f)  
            if len(data) != (myCounter + 1): 
                f.write("," + "\r\n")
            else:
                f.write("]")  
        myCounter += 1  
        print (str(myCounter) + "/" + str(len(data)))
    

print("So viele Datensaetze wurden korrigiert: " + str(countryFoundCounter))