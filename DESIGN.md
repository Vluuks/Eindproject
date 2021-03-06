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
* Mogelijkheid op het woord te tikken om de Engelse vertaling te zien te krijgen of dat het woord voorgelezen word in het Nederlands.
* Verwijswoorden oefenen in plaats van lidwoorden.


## Classes & methods MVP

### Class Preparation
Klasse die de opties laadt en het woordenboek inlaadt.

* loadDictionary
returns set van key/value pairs met daarin de woorden en hun Engelse vertaling (inlezen van XML)

* loadOptions
returns de gekozen spelmodus chill of normaal en andere opties (indien van toepassing)






### Class GamePlay (MainActivity)
Klasse die de belangrijkste gameelementen bevat en het verloop van het spel regelt.

* Heeft de context en text/imageviews van MainActivity nodig bij constructor.

* initializeGame
verkrijg de dictionary/keylist die in preparation is aangemaakt en initalize de beginvalues van de game op basis van de opties

* pickWord
argument is waarschijnlijk de dictionary of keyset
returns een random gekozen woord uit de dictionary en de bijbehorende vertaling

* splitWord (onderdeel van pickword)
woord als argument
zet de TextView naar het zelfstandig naamwoord en bewaart het lidwoord om te checken

* initializeTimer
Maakt een timer aan en zorgt er ook voor dat deze naderhand gestopt of gepauzeerd wordt indien nodig.

* checkArticle
vergelijkt user input via button met het lidwoord dat bij het zelfstandig naamwoord hoort
huidige woord als argument, als het woord goed geraden is verwijder het uit de lijst
als een woord fout geraden wordt, voeg het dan nogmaals toe aan de keylist om de kans te vergroten dat het nog een keer langskomt

* ifCorrect, ifIncorrect
Handelt het antwoord van de gebruiker verder af.

* updateScore (onderdeel van checkArticle)
scoregerelateerde TextViews vanuit mainactivity als argument
returns de nieuwe score en/of hoeveelheid levens van de gebruiker na check

* onWin/onLose
score/resterende levens/gamemode van gebruiker als argumenten
verwijs gebruiker door naar de achievement/score activity


### MainActivity

* displayHints
* goToOptions
* goToAchievements
* displayTranslation
* goToShop
* startNewGame (verwijst naar Gameplay)


### OptionsActivity
* listeners voor de switches/checkbuttons
* saveOptions
* Sla de gekozen opties van de gebruiker op als deze een wijziging maakt in de switch.


### AchievementsActivity

* checkForAchievement
getriggerd wanneer gebruiker vanuit game komt
controleer of de behaalde score van de gebruiker voldoende is om een achievement te halen, zo ja maak daar melding van en markeer achievement als compleet
sla de status van de achievements op in SharedPreferences

* loadAchievements (onCreate)
haal de status van de achievements uit SharedPreferences en gebruik deze

* saveAchievements
zet achievements in sharedpreferences in de vorm van json array






## Interface elementen gekoppeld aan methods
![](doc/designdoc1.JPG)

![](doc/designdoc2.JPG)




