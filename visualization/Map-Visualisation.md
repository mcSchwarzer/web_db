## Visualisierung mit React-Google-Maps
Um zunächst die UFO-Sichtungen geographisch einordnen zu können, lag zunächst das Ziel vor, alle Sichtungen auf eine Karte zu bringen. 
Es wurde auf das Java-Script-Frameworkt React von Facebook zugegriffen. Eine Möglichkeit diese Daten mit React zu visualisieren, ist die Google-Maps-Component von Tom Chen (https://github.com/tomchentw/react-google-maps).

Um den Server zu starten, benötigen sie den Node-Package-Manager (NPM) und führen dieses Repository mit ```npm start``` aus.
Dabei wurde deutlich, dass viele UFO-Sichtungen bei den Koordinaten ```0,0``` lagen, also mitten im Atlantik. Es kann davon ausgegangen werden, dass diese Koordinaten fehlerhaft sind und somit werden diese Datensätze herausgefiltert.
