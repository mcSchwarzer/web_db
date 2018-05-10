#!/bin/bash

java -jar weather.jar

./elasticSearchWeather.sh
./src/wetterZwischenergebnisse/correl.sh
./src/wetterZwischenergebnisse/ihmlCurl.sh
python weather.py