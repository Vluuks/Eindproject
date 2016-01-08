## Day 2
* Begonnen aan design document.
* Bepaald welke methods and classes ik ga gebruiken.
* XML parser basis gemaakt (vooralsnog alleen Nederlands).
* Basiselementen layout app gemaakt. 


## Day 3
* Design document verder uitgewerkt: meer nagedacht over wat een handige indeling is qua classes.
* Random item uit dictionary picker gemaakt na eerst de keys in een ArrayList te hebben gestopt.
* dit item gesplitst in lid- en zelfstandig naamwoord en de mogelijk extra definities buiten beschouwing gelaten.
* checkfunctie lidwoord en scoresysteem gemaakt en puntensysteem ingevoerd.
* Toch gekozen voor een vaste hoeveelheid levens om de achievements betekenisvol te houden.
* Ga nu een timer of teller invoeren zodat er toch een einde is aan de app ook al doe je alles goed.
* Vertaling als je er op tikt staat even on hold ivm XMLparser die veel problemen geeft. Ga eerst verder met het echte spel uitwerken.
* Ontdekt dat er soms woorden inzitten waarbij zowel de als het goed zouden zijn. Bijvoorbeeld stof. Deze ga ik er waarschijnlijk uithalen.


## Day 4
* Hints toegevoegd, toch maar in aparte Activity omdat die beter strookt met de rest van de app.
* Verbindingen tussen alle activities en data doorgeven verloopt goed.
* Teller ingevoerd, vooralsnog op 2 minuten gezet. Dit zal het makkelijker maken om vergelijkbare highscores te introduceren omdat snelheid ook een rol speelt.
* Goed geraden woorden worden uit de keylist gehaald en fout geraden woorden nogmaals toegevoegd, hierdoor is de kans veel groter dat foute woorden nogmaals langskomen.
* Deze keylist vooralsnog is per game nieuw, dus je wordt niet eindeloos achtervolgd door je fouten. Misschien maak ik het nog zo dat de foute woorden wel blijven bestaan en die herhaald blijven worden, ook bij volgende spellen. Dan moet er wel een optie zijn om "fris" te beginnen.
* Lege functies voor achievements zijn geschreven, moet nu nadenken over welke achievements en hoe deze efficiënt te checken.
* Chill mode zonder timer, zonder score/combo en zonder levens. Hierbij ben je niet eligible voor highscores of achievements, is puur om voor jezelf te oefenen. Misschien dat ik nog aparte chill mode achievements toevoeg als ik daar tijd voor heb.
* Heb besloten on onwin/onlose altijd door te sturen naar achievements na een passend bericht/toast. De back button in achievements moet dan een nieuw spel starten. 
* Er zit geen beginscherm met opties/nieuw spel/highscores etc. in, heb deze bovenaan in de game zelf gestopt, minder omslachtig en past prima omdat het design van de game zelf vrij rustig en leeg is.
* Timer is nog wel erg buggy, bij heen en weer gaan tussen activities resumet ie wel maar gaan er op 1 of andere manier 2 door elkaar lopen. Lichtelijk bizar.


## Day 5
* Poging gedaan om timer te fixen, lijkt er op dat de bug in de API van android zit (cancel werkt niet) op zoek gegaan naar andere mogelijkheden. 
* Verwijzing van backbutton vanuit onwin achievement screen verbeterd: start nu een nieuw spel in plaats van je de hele tijd heen en weer te sturen.
* Bij chill mode moet nog een feedback of het goed of fout is, die ontbreekt nu en dan heeft het oefenen ook niet veel zin.

