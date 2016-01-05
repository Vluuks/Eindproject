# Design document

## DeHet MVP functionaliteiten
* Inladen van woordenboek
* Kiezen van random woord uit woordenboek
* Splitsen van lidwoord en zelfstandig naamwoord
* Controleren of lidwoord gekozen door user klopt
* Basic scorefunctie (teller goede dan wel foute antwoorden)


## DeHet extra functionaliteiten

* Puntensysteem in plaats van simpele teller
* Timer die afloopt zodat de game altijd een einde heeft, ook al maakt de gebruiker nooit een fout (optimale tijd nader te bepalen)
* Combosysteem: meerdere achter elkaar goed is meer punten
* Woorden waarbij een fout wordt gemaakt komen vaker terug
* Achievements/badges om gebruiker te motiveren
* Hint panel met tips om te bepalen welk lidwoord gekozen moet worden
* Chill mode: geen levens verliezen, eindeloos fouten maken mogelijk
* Wissen van gegevens: bijvoorbeeld opgeslagen veelgemaakte fouten
* Mogelijkheid op het woord te tikken om de Engelse vertaling te zien te krijgen



## Classes & methods MVP

### Class Initialization (MainActivity)
* loadDictionary
⋅⋅⋅ returns set van key/value pairs met daarin de woorden en hun Engelse vertaling (inlezen van XML)

* getStatus
⋅⋅* returns de gekozen spelmodus chill of normaal (indien van toepassing)

### Class GamePlay (MainActivity)
* pickWord
⋅⋅* argument is waarschijnlijk de dictionary
⋅⋅* returns een random gekozen woord uit de dictionary

* splitWord
⋅⋅* woord als argument
⋅⋅* zet de TextView naar het zelfstandig naamwoord en bewaart het lidwoord om te checken

* checkArticle
⋅⋅* vergelijkt user input via button met het lidwoord dat bij het zelfstandig naamwoord hoort

* addWordToRepeat
⋅⋅* huidige woord als argument
⋅⋅* als een woord fout geraden wordt, voeg het dan nogmaals toe aan de dictionary om de kans te vergroten dat het nog een keer langskomt

* updateScore
⋅⋅* score TextView als argument
⋅⋅* returns de nieuwe score en/of hoeveelheid levens van de gebruiker na check

* onGameEnd
⋅⋅* score/resterende levens/gamemode van gebruiker als argumenten
⋅⋅* verwijs gebruiker door naar de achievement/score activity
⋅⋅* controleer of de behaalde score van de gebruiker voldoende is om een achievement te halen, zo ja maak daar melding van en markeer achievement als compleet
⋅⋅* sla de status van de achievements op in SharedPreferences


### OptionsActivity
* saveOptions
⋅⋅* Sla de gekozen opties van de gebruiker op als deze een wijziging maakt in de switch.


### AchievementsActivity
* loadAchievements
⋅⋅* haal de status van de achievements uit SharedPreferences






## Interface elementen gekoppeld aan methods
![](TODO)



