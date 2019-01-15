# Process book: Streeplijst  

**Dag 1 (08-01-2019)**   
De eerste schetsen zijn gemaakt. Besloten om te beginnen met een overzicht van de producten (startscherm) en
pas op het tweede scherm de gebruiker te kiezen. Oorspronkelijk was het idee om te beginnen met 
het kiezen van de gebruiker, en daarna het product te kiezen. Reden hiervoor is dat het niet de bedoeling 
is dat gebruiker bij één bestelling/streep verschillende producten streept, maar het wel mogelijk moet zijn om in één
keer hetzelfde product voor meerdere gebruikers te strepen.

   
**Dag 2 (10-01-2019)**   
De eerste activiteiten en classes (Product & User) zijn gemaakt. Via het productenoverzicht kan je doorklikken
naar het gebruikersoverzicht. Door een producten of gebruiker lang ingedrukt te houden kan je ze verwijderen. Het strepen en/of 
verwijderen van een product/gebruiker wordt bevestigd met een Toast.

**Dag 3 (11-01-2019)**   
Activiteiten toegevoegd voor het registreren van nieuwe gebruikers en het toevoegen van producten. Deze werken nog niet.
Daarnaast een overzichtspagina toegevoegd die in de toekomst een overzicht moet geven van de totale kosten en transacties.
De toasts voor het verwijderen van producten en gebruikers zijn vervangen door AlertDialog's. Hierdoor moet de gebruiker
uitdrukkelijk bevestigen dat het de bedoeling is om een product te verwijderen. Oorspronkelijk was het idee
om in de Actionbar een menuknop op te nemen die zou linken naar een 'MenuActiviteit', nu is er voor gekozen om dat niet
te doen maar gewoon een uitschuif menu te gebruiken. Daarnaast is in de Actionbar een 'Home-knop' opgenomen
die de gebruiker terug brengt naar het productenoverzicht.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/AlertDialog.PNG" height="5%" width="40%"/> <img
  src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/ActionbarMenu.PNG" height="5%" width="40%"/>  
</p>


 **Dag 4 (12-01-2019)**   
 De database en de eerste twee tabellen zijn aangemaakt: één voor producten en één voor de gebruikers. Producten en gebruikers
kunnen nu handmatig worden toegevoegd. Het toevoegen moet wederom worden bevestigd d.m.v. een AlertDialog. Daarnaast besloten om te 'strepen' door gebruikers aan te klikken en vervolgens middels een knop te bevestigen als de bestelling compleet is. Oorspronkelijk was het idee dat de gebruiker maar één keer geselecteerd kon worden. Dit maakt het echter onmogelijk om meerdere keren op één gebruiker te strepen. Daarom is nu het idee dat je een gebruiker meerdere keren aan kan klikken. Naast het gebruikersoverzicht zal dan in een 
ListView een overzicht worden gegeven van de huidige bestelling.

**Dag 5 (13-01-2019)**   
Producten en gebruikers kunnen nu ook handmatig worden verwijderd. Oorspronkelijk was het idee dat producten en gebruikers verwijderd konden worden door ze lang ingedrukt te houden. Nu wordt de gebruiker eerst doorverwezen naar de profielpagina van de gebruiker of het product. Via deze pagina kan het product of de gebruiker dan worden verwijderd. De reden hiervoor is dat het idee al was om  profielpagina's te hebben voor producten en gebruikers zodat in de toekomst via deze pagina's transacties kunnen worden verwijderd, of prijzen kunnen worden veranderd. Over het algemeen zal van deze functies waarschijnlijk vaker gebruik worden gemaakt dan het verwijderen van producten/gebruikers, het is daarom van belang dat het eenvoudig is om op deze profielpagina's te komen.

**Dag 6 (14-01-2019)**   
De tabel voor het transactieoverzicht is toegevoegd aan de StreepDatabase. Daarbij is besloten om een aparte 'Transactie-class' aan te 
maken. De reden hiervoor is dat er momenteel veel wordt opgeslagen in de transactietabel (transactieId, gebruikerId, gebruikersnaam, productnaam, productprijs, hoeveelheid, totaalprijs, gestreept, timestamp). Het is daarom overzichtelijker om hier een aparte class voor te creëren waarbinnen bijvoorbeeld wordt uitgerekend wat de totaalprijs is. Achteraf zal het wellicht niet noodzakelijk blijken dat al deze informatie wordt opgeslagen. Het idee is, dat het waarschijnlijk eenvoudiger is om later in het proces kolommen te schrappen, dan om ze dan nog toe te voegen indien ze toch nodig blijken.
Middels een HashMap kan nu worden bijgehouden hoevaak een gebruiker is aangeklikt. Tot slot is besloten om tijdens het strepen geen overzicht van de bestelling naast het gebruikersoverzicht weer te geven. In plaats daarvan is het de bedoeling dat geselecteerde gebruikers van kleur veranderen en er naast hun naam verschijnt hoevaak ze zijn geselecteerd.


