#!/bin/bash

python3 educationcorrelation.py

python3 ufo_location_correction.py
java -jar Data_Formatter.jar
python3 deathmortalitycorrelation.py
printf "Es folgt die Erstellung der NDJSON-Datei\n"
java -jar ToNdJson.jar

printf "_________________________________________________________________________\n"
printf "Sie finden die ausgewerteten Korrelationsdateien Dateien im Ordner /docs \n"