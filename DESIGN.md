# Design document

## DeHet MVP functionaliteiten
* Inladen van woordenboek
* Kiezen van random woord uit woordenboek
* Splitsen van lidwoord en zelfstandig naamwoord
* Controleren of lidwoord gekozen door user klopt
* Basic scorefunctie (teller goede dan wel foute antwoorden)


## DeHet extra functionaliteiten

### Diversen
* Puntensysteem in plaats van simpele teller
* Combosysteem: meerdere achter elkaar goed is meer punten
* Woorden waarbij een fout wordt gemaakt komen vaker terug
* Achievements/badges om gebruiker te motiveren
* Hint panel met tips om te bepalen welk lidwoord gekozen moet worden

### Gebruikersopties
* Kiezen hoeveelheid levens
* Chill mode: geen levens, eindeloos fouten maken mogelijk
* Wissen van gegevens: bijvoorbeeld opgeslagen veelgemaakte fouten



## Classes & methods MVP

### Initialization
* loadDictionary
* getStatus (resuming/getting settings from sharedpreferences)

### GamePlay
* pickWord
* splitWord
* checkArticle
* updateScore

## Interface elementen gekoppeld aan methods
![](TODO)



