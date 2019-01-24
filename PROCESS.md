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
 De database (StreepDatabase) en de eerste twee tabellen zijn aangemaakt: één voor producten en één voor de gebruikers. Producten en gebruikers kunnen nu handmatig worden toegevoegd. Het toevoegen moet wederom worden bevestigd d.m.v. een AlertDialog. Daarnaast besloten om te 'strepen' door gebruikers aan te klikken en vervolgens middels een knop te bevestigen als de bestelling compleet is. Oorspronkelijk was het idee dat de gebruiker maar één keer geselecteerd kon worden. Dit maakt het echter onmogelijk om meerdere keren op één gebruiker te strepen. Daarom is nu het idee dat je een gebruiker meerdere keren aan kan klikken. Naast het gebruikersoverzicht zal dan in een 
ListView een overzicht worden gegeven van de huidige bestelling.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/profiel.png" height="5%" width="20%"/>  
</p>


**Dag 5 (13-01-2019)**   
Producten en gebruikers kunnen nu ook handmatig worden verwijderd. Oorspronkelijk was het idee dat producten en gebruikers verwijderd konden worden door ze lang ingedrukt te houden. Nu wordt de gebruiker eerst doorverwezen naar de profielpagina van de gebruiker of het product. Via deze pagina kan het product of de gebruiker dan worden verwijderd. De reden hiervoor is dat het idee al was om  profielpagina's te hebben voor producten en gebruikers zodat in de toekomst via deze pagina's transacties kunnen worden verwijderd, of prijzen kunnen worden veranderd. Over het algemeen zal van deze functies waarschijnlijk vaker gebruik worden gemaakt dan het verwijderen van producten/gebruikers, het belang om eenvoudig op deze pagina's te komen is daarom groter dan het belang om eenvoudig producten/gebruikers te verwijderen. 

**Dag 6 (14-01-2019)**     
De tabel voor het transactieoverzicht is toegevoegd aan de StreepDatabase. Daarbij is besloten om een aparte 'Transactie-class' aan te 
maken. De reden hiervoor is dat er momenteel veel wordt opgeslagen in de transactietabel (transactieId, gebruikerId, gebruikersnaam, productnaam, productprijs, hoeveelheid, totaalprijs, gestreept, timestamp). Het is daarom overzichtelijker om hier een aparte class voor te creëren waarbinnen bijvoorbeeld wordt uitgerekend wat de totaalprijs is. Achteraf zal het wellicht niet noodzakelijk blijken dat al deze informatie wordt opgeslagen. Het idee is, dat het waarschijnlijk eenvoudiger is om later in het proces kolommen te schrappen, dan om ze dan nog toe te voegen indien ze toch nodig blijken.
Middels een HashMap kan nu worden bijgehouden hoevaak een gebruiker is aangeklikt. Tot slot is besloten om tijdens het strepen geen overzicht van de bestelling naast het gebruikersoverzicht weer te geven. In plaats daarvan is het de bedoeling dat geselecteerde gebruikers van kleur veranderen en er naast hun naam verschijnt hoevaak ze zijn aangeklikt.


**Dag 7 (15-01-2019)**  
De transacties worden nu bijgehouden in de database. Op de profielpagina van een gebruiker zijn de transacties van desbetreffende gebruiker weergegeven, op de kostenoverzicht-pagina is een overzicht te vinden van alle transacties. Daarnaast wordt op deze pagina weergegeven wat de totale uitgaven zijn. Vanaf de profielpagina kunnen transacties worden verwijderd. Tot slot besloten om ook gebruikersportfolio's bij te houden in de StreepDatabase zodat de gebruiker in kan zien waaraan zijn of haar geld is uitgegeven (nog niet in uitvoering gebracht). Het transactieoverzicht dient met name om het verwijderen van transacties mogelijk te maken. Naast een overzicht van de totale kosten en een transactieoverzicht, zal dus ook het portfolio moeten worden weergegeven op de profielpagina's. 

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/userTransactions.PNG" height="5%" width="40%"/>  
</p>

**Dag 8 (16-01-2019)**  
De kosten in de gebruikerstabel worden nu aangepast bij het verwijderen van transacties. Tijdens het strepen wordt weergegeven hoevaak een gebruiker is geselecteerd.   

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/selectUsers.PNG" height="5%" width="50%"/>  
</p>

**Dag 9 (17-01-2019)**    
De users table uit de StreepDatabase kan worden omgezet in een csv bestand en vervolgens worden gemaild. Oorspronkelijk was het plan om het csv bestand vervolgens om te zetten in een xls bestand. Nu blijkt echter dat het boekhoudprogramma dat wordt gebruikt om de tabellen om te zetten in facturen, ook csv bestanden accepteert. Converteren naar xls is daarom onnodig.

**Dag 10 (18-01-2019)**    
Hoewel dit oorspronkelijk niet het plan was, staat de app nu vast in portretmodus. De reden hiervoor is dat het op deze manier eenvoudiger is om te voorkomen dat het selecteren van de gebruikers niet juist wordt geregistreerd wanneer de tablet gedraaid wordt. Bovendien is het de bedoeling dat de tablet ergens op een vaste plek komt te hangen en dus ook niet in een andere modus dan portretmodus gebruikt zal worden. Daarnaast is het nu mogelijk om een standaard e-mail adres op te geven waarnaar de bestanden worden gemaild. Voor het mail adres is een aparte tabel aangemaakt in de StreepDatabase.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/addEmail.PNG" height="5%" width="30%"/> <img
   src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/ExportMail.PNG" height="5%" width="50%"/>                   </p>                                                                                                   

**Dag 11 (19-01-2019)**   
Voor iedere gebruiker wordt nu een portfolio bijgehouden in de StreepDatabase. Via de profielpagina kan een gebruiker doorklikken naar zijn of haar portfolio.    

**Dag 12 (20-01-2019)**   
Bij het toevoegen van een product kan nu een foto worden toegevoegd vanuit de galerij. Deze foto's worden weergegeven in het producten overzicht (ProductsActivity).

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/uploadProductPic.jpg" height="5%" width="25%"/> <img
   src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/uploadedProductPic.jpg" height="5%" width="25%"/> <img
   src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/productsPictures.jpg" height="5%" width="25%"/>
</p>   

**Dag 13 (21-01-2019)**   
Bij het toevoegen van een gebruiker kan nu ook een foto worden toegevoegd die wordt weergegeven in het gebruikersoverzicht en op de profielpagina. Daarnaast besloten om het gebruikersoverzicht te sorteren op gebruikersnaam, en een zoekbalk toe te voegen zodat gebruikers gemakkelijk te vinden zijn. Dit met het oog op gebruiksvriendelijkheid bij een groot aantal gebruikers.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/usersPics.jpg" height="5%" width="25%"/> <img
   src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/processDoc/searchbar.jpg" height="5%" width="25%"/> 
</p>

** Dag 14 (22-01-2019)**      
Picasso gebruikt om de foto's te laden zodat de GridViews niet zo langzaam zijn. Bij het exporteren van de tabellen wordt nu niet enkel de gebruikerstabel maar ook de portfoliotabel verzonden.   


** Dag 15 (23-01-2019)**
Dingen aan de layout veranderd. Foto's vervormen niet meer, en aantal normale knoppen vervangen door floating action buttons.    


** Dag 16 (24-01-2019)**
Er kan nu een pin worden ingesteld, die nodig is om produc ten te verwijderen.   


