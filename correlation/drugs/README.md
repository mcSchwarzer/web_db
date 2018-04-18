# Korrelation der Todesfälle durch übermäßigen Drogenkonsum mit der Anzahl der Ufo-Sichtungen pro Bundesstaat in den USA

Als Grundlage liegt ein Datensatz der US-Regierung vor, in dem die Anzahl der Todesfälle zwischen 1999 bis 2015.



| State        | Year           | Sex  | Age Group |Race and Hispanic Origin|Deaths|Population|Crude Death Rate|Standard Error for Crude Rate|Lower Confidence Limit for Crude Rate|Upper Confidence Limit for Crude Rate|Age-adjusted Rate|Standard Error for Age-adjusted Rate|Lower Confidence Limit for Age-adjusted Rate|Upper Confidence Limit for Age-adjusted Rate|State Crude Rate in Range|US Crude Rate|US Age-adjusted Rate|
| -----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|
| ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... | ... |


Die Python-Bibliothek Numpy berechnet den Korrelationskoeffizienten von Bravais und Pearson. Dazu sind zwei Arrays benötigt, die dieselbe Länge besitzen.
 

```python
import numpy
...
numpy.corrcoef(Array_X, Array_Y)
```
Als Ausgabe erhält man ein Array folgenden Aufbaus:
```python
[[Kor. x mit x][Kor. x mit y]]
[[Kor. y mit x][Kor. y mit y]]
```

Quelle:
https://catalog.data.gov/dataset/drug-poisoning-mortality-by-state-united-states
