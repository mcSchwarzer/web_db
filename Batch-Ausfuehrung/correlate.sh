#!/bin/bash

python3 educationcorrelation.py

python3 state.py
java -jar test.jar
python3 testing.py
printf "Es folgt die Erstellung der NDJSON-Datei\n"
java -jar tondjson.jar

printf "_________________________________________________________________________\n"
printf "Sie finden die ausgewerteten Korrelationsdateien Dateien im Ordner /docs \n"