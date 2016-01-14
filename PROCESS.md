# Week 1

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



# Week 2

## Day 6
* Timer maar even gelaten voor wat het is, eerst achievement systeem geimplementeerd.
* Gekozen voor listview van objects, zo kan ik de onderdelen van het achievement gemakkelijk aanpassen.
* Wilde een switch toepassen voor de achievement check, helaas kan in een switch geen gebruik gemaakt worden van groter/kleiner dan symbolen dus dit viel af. Nu is het een reeks if statements. Minder mooi, maar helaas noodzakelijk.
* Achievements vooralsnog onderverdeeld in makkelijker en moeilijker.
* Als timer weer werkt ook achievments toevoegen zoals score X binnen Y seconden.
* Teller met goede en foute antwoorden toegevoegd om wat meer een idee te geven van de performance van de user en ook chill mode zinvolle feedback te geven (die ontbrak tot nog toe).
* Geprobeerd om volledig scherm imageview te tonen on Win/Lose met passende boodschap en plaatje, dit is helaas nog niet gelukt ivm een memory error.
* Ingelezen over het opslaan van achievements. Omdat een arraylist niet zonder meer in sharedpreferences opgeslagen kan worden moet ik hier iets anders voor verzinnen.
* Nagedacht over een mechanic om levens terug te krijgen, bijvoorbeeld bij een combo van x25 +1 leven (max blijft wel 3). Dit om combo's nog meer te belonen.
* Schetsen gemaakt plaatjes en icoontjes die ik aan het eind graag wil gaan gebruiken. 


## Day 7
* Text to speech geimplementeerd in app, was nogal een gedoe, maar uiteindelijk blijkt Pico API helaas geen ondersteuning te bieden voor Nederlands... Beetje zonde van de tijd dus.
* Nog op zoek naar een API die dat mogelijk wel (gratis) doet.
* Begonnen met een indeling van model classes te maken om zo de code leesbaar te houden. (onder andere GamePlay, GamePreparation, Interface)
* Uitzoeken hoe ik min ArrayList kan omzetten naar Gson om dit op te slaan in sharedpreferences of weg te schrijven naar een text file.
* Gson API geinstaleerd van Google.
* Structuur voor het opslaan en inladen van achievements via SharedPreferences gemaakt. Gson gaat nog niet helemaal goed, omzetten naar Array mislukt een beetje. Maar begin is er.
* Plaatjes van achievement gedragen zich nu correct bij het wel of niet behalen en verwdwijen ook als de achievement data verwijderd wordt.


## Day 8
* Opslaan en inladen van achievements via SharedPreferences met Gson werkt eindelijk. 
* Achievements kunnen nu worden herhaald om daarmee punten te verdienen. Met deze punten kan in de shop (WIP) wat gekocht worden voor de mascotte. 
* Mascotte moet nog wel gemaakt worden, ga ik volgende week vooral mee bezig, dan wil ik echt de lay out componenten leuk maken.
* Moeilijkere achievements geven meer punten dan makkelijke bij herhaling (min 5 max 50).
* Gestart met het zelf opzetten van een timer die niet de countdowntimer API gebruikt. 
* Ik ben van plan om ook de verwijswoorden (die/dit/deze/dat) een aparte mode te geven zodat ook daarmee geoefend kan worden.
* Het lijkt erop dat de timer die ik in een aparte class heb aangemaakt (met hulp van StackOverflow) het nu wel goed doet. Pauzeren en resume werkt.


# Day 9
* Begonnen met de app in classes te verdelen. Op dit moment is er alleen een GamePlay class. De app werkt nu wel en MainActivity is een stuk leger.
* Kijken of de HighScoreActivity (achievements) ook verdeeld kan worden in classes op een handige manier.
* Schets gemaakt van de classes, deze ga ik toevoegen aan het DesignDocument.  
* Begonnen met het bedenken van icoontjes en plaatjes die ik volgende week wil toevoegen aan de app.
* Mode geimplementeerd waarbij de demonstrative pronouns geoefend kunnen worden in plaats van de lidwoorden.
* Testplaatje onlose/onwin geimplementeerd, nog op zoek naar hoe ik deze zichtbaar/onzichtbaar kan maken op een handige manier.