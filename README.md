# KFZ Labeler

Für den Prototyp zur Bildklassifikation werden klassifizierte Bilder benötigt, d.h. die Zielklassen, die durch das neuronale Netz erkannt werden sollen, müssen bekannt sein. Dazu müssen die Bilder in verschiedene Ordner, die den Zielklassen entsprechen, eingeordnet werden.

## Vorraussetzungen
Die Bilder müssen als einzelne Bilddateien (z.B. jpg, png, bmp, tif) vorliegen. Die Bilder müssen anonymisiert sein, d.h. es dürfen weder Personen auf den Bildern erkennbar sein noch Fahrzeugkennzeichen.

## Klassifizierung von Bilddateien
Im Konzeptionsworkshop am 24. April 2017 in Stuttgart wurde vereinbart, dass die Bildklassifikation im Bereich »Schadenlokalität an Fahrzeugen« erprobt werden soll. Im ersten Schritt soll erkannt werden, aus welcher Richtung ein Fahrzeug auf einem Bild zu sehen ist und ob auf dem Bild ein Schaden am Fahrzeug zu sehen ist oder nicht. Dabei sind 8 Richtungen zu unterscheiden: vorn, vorn links, links, hinten links, hinten, hinten rechts, rechts, vorn rechts. Darüber hinaus soll versucht werden, auch noch die Position des Schadens am Fahrzeug zu bestimmen, dabei sollen ebenfalls die oben genannten 8 Richtungen verwendet werden.

## Ordnerstruktur für die Klassifizierung
Für die Einordnung der Bilder wird die Ordnerstruktur in Abbildung 1 vorgeschlagen. Die Ordnerstruktur steht auf der Projektplattform im Ordner Dateien / AP 3 Big Data / AP 3 Anwendungstrack / Prototyp Bildklassifikation als Zip-Datei »Schadenbilder InnoNetz Ordnerstruktur« zur Verfügung. Auf der obersten Ebene werden die 8 Richtungen unterschieden. In jedem dieser 8 Ordner gibt es einen Unterordner »keinSchaden«, in dem Bilder von unbeschädigten Fahrzeugen (genauer: Bilder, auf denen kein Schaden am Fahrzeug erkennbar ist) abgelegt werden. Weiterhin sind in jedem der 8 Ordner 5 Unterordner für die möglichen Schadenpositionen. Es sind jeweils nur 5 Unterordner, weil davon ausgegangen werden kann, dass nicht alle Kombinationen von Aufnahmerichtung und Schadenposition vorkommen, z.B. kann auf einer Fahrzeugaufnahme von hinten kein Schaden erkannt werden kann, der sich am Fahrzeug vorn befindet. Sollten derartige Fälle auftreten, kann die Ordnerstruktur um die nötigen Unterordner ergänzt werden.

## Benutzung dieses Tools
Dieses Tool vereinfacht diesen Prozess, indem es die Bilder in einem ausgewhälten Ordner nacheinander öffnet. Jedes Bild kann durch einen Code gelabelt werden, der unten in das Textfeld eingegeben wird. Am oberen Rand wird die Codierung Schematisch dargestellt. Die Zahlen auf dem Numblock Repräsentieren die Richtungen des Schadens. 1 und 0 Zeigen an, ob ein Schaden vorhanden ist oder nicht. Die Reihenfolge für die Zahlen ist:
1. Richtung der Ansicht aus Fahrerperspektive
2. Schaden vorhanden
3. Position des Schadens

Das Tool Sortiert das Bild dann automatisch richtig. **Bilder die nicht verwendbar sind**, weil sie z.B. zu wenig vom Auto zeigen oder ein Gesicht in einer Spiegelung zeigen, werden mit 5 gelabelt.

### Beispiele
Code | Bedeutung
-----|---------
`817` | Ansicht vorne, Schaden vorne links
`20` | Ansicht hinten, Kein Schaden
`619` | Ansicht rechts, Schaden vorne reichts
`414` | Ansicht links, Schaden links
`5` | Nicht Kategorisierbar

## Starten des Tools - Windows
Das Programm liegt im Ordner build, und kann über die `KFZ_Labeler.bat` gestartet werden.

## Starten des Tools - Linux
Das Programm liegt im Ordner build, und kann über die `KFZ_Labeler.sh` gestartet werden.
