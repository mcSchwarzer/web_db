#!/bin/bash

python edu_correlation.py
python ufo_location_correction.py
java -jar Data_Formatter.jar
python deathmortalitycorrelation.py
python correlation_ufo_and_sundp500_data.py

printf "Es folgt die Erstellung der NDJSON-Datei\n"
java -jar ToNdJson.jar

printf "_________________________________________________________________________\n"
printf "Sie finden die ausgewerteten Korrelationsdateien Dateien im Ordner /docs \n"