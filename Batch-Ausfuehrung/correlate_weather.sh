#!/bin/bash

java -jar weather.jar

./elasticSearchWeather.sh
./src/wetterZwischenergebnisse/correl.sh
./src/wetterZwischenergebnisse/ihml.sh
python weather.py