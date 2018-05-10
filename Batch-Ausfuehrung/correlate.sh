#!/bin/bash

python3 edu_correlation.py
python3 ufo_location_correction.py
java -jar Data_Formatter.jar
python3 deathmortalitycorrelation.py
python3 correlation_ufo_and_sundp500_data.py

printf "Es folgt die Erstellung der NDJSON-Datei\n"
java -jar ToNdJson.jar

java -jar weather.jar
./docs/correl.sh
./docs/cmdFileForW.sh
python3 weather.py


printf "_________________________________________________________________________\n"
printf "Sie finden die ausgewerteten Korrelationsdateien Dateien im Ordner /docs \n"
