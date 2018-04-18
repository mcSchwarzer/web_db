import io
import reverse_geocoder as rg
import json

myCounter = 0
countryFoundCounter = 0

with open('<ufo data with faulty country values path>') as data_file:
    data = json.load(data_file)
    for item in data:
        if item["country"] == "":
            #geo = rg.RGeocoder(mode=2, verbose=True, stream=io.StringIO(open('C:\\Users\\JZimny\\Desktop\\DH\\Skripte\\4. Semester\\reichi projekt\\UFO Daten\\rg_cities1000.csv', encoding='utf-8').read()))
            if not item["lat"] == 0 and not item["lon"] == 0:
                coordinates = (item["lat"], item["lon"])
                results = rg.search(coordinates, mode=1)
                item["country"] = list(results[0].items())[5][1].lower()
                countryFoundCounter += 1
        with open("<Path where processed ufo data json should be saved>", 'a') as f:
            if myCounter == 0:
                f.write("[\r\n")
            json.dump(item, f)  
            if len(data) != (myCounter - 1): 
                f.write("," + "\r\n")
            else:
                f.write("]")  
        myCounter += 1  
        print (str(myCounter) + "/" + str(len(data)))
print("So viele Datensaetze wurden korrigiert: " + str(countryFoundCounter))
