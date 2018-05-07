# UFO-Daten
Der UFO-Datensatz war in einem .csv-Format vorhanden, war aber beschädigt, da er in einigen Zeilen eine Datenspalte zu viel beihnaltete. 
Der Einfachheit halber haben wir diese fehlerhaften Datensätze vernachlässigt bzw. haben diese entfernt. 
Daraufhin wurde die reparierte Datendatei in eine .json-Datei umformattiert, was keinen speziellen Grund hat, nur der Erfahrung der Gruppenmitglieder mit diesem Format. 
Dazu wurde ein Online-Formatter verwendet: https://www.freeformatter.com/batch-formatter.html
Die Datei die dieser erstellt finden Sie unter: https://drive.google.com/open?id=1eSVekQTN42jVxzbF8riCtnvnrrtH61Fl oder die Datei Max/fulldata.json.zip, die Sie bitte zunächst entpacken müssen.

Um den Datensatz mit den anderen Datensätzen später korrelieren zu können, mussten die Staaten in den USA auch mit vollem Namen in den Datensatz eingefügt werden. Dies wurde mit den Methode ```addStatenames()``` über eine separate Datendatei mit den Übersetzungen der Abkürzungen in den kompletten Namen ermöglicht. 

Ein weiterer Fehler in dem Datensatz sind manche Uhrzeiten. Dabei wurde bei manchen Datensätzen die Uhrzeit 24:00 Uhr angegeben, was so nicht vorkommen kann und von maschineller Verarbeitung nicht erkannt wird. Da diese Uhrzeit Interpretationsspielraum bietet, ändert die Methode ```repairdates()``` die Uhrzeit auf 00:00 Uhr.

Des Weiteren ist in den Datensätzen der Ländername oder (Bundes-)Staatenname angegeben sondern nur die Koordinaten. Dazu hilft der Reverse-Geocoder, eine Python-Bibliothek (https://pypi.org/project/reverse_geocoder/), die aufgrund der Koordinaten den Staat und das Land sucht und zurückgibt. Dies wurde in der Datei state.py getan. 
