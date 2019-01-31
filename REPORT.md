# Report: Streeplijst

*Anne Hoogerduijn Strating*  
*12441163*  

**Samenvatting**  
Indien een groep gezamenlijk drank ingekoopt kan met deze app worden bijgehouden wie wat heeft gedronken zodat de kosten aan het einde van een door gebruikter te bepalen termijn gemakkelijk kunnen worden verrekend.  

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/ProductsActivity.jpg" height="5%" width="24%"/> 
</p>  

## Design
Het beginscherm van de app is het productenoverzicht. Hier selecteer je een product. Vervolgens selecteer je de gebruiker(s) waarop je het product wil strepen. Tot slot druk je op de 'Streep knop' en dan is het product gestreept. Wanneer je een product in het productenoverzicht langer ingedrukt houdt, ga je naar de profielpagina van het product.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/DesignHoofd.PNG" height="5%" width="80%"/> 
</p>   

Via de Navigation Drawer kan er worden doorgeklikt naar alle andere benodigde activiteiten. De gebruiker kan het transactieoverzicht inzien. Hier kunnen transacties ook worden verwijderd. Daarnaast kunnen er nieuwe producten en gebruikers worden toegevoegd. Ook kan er een PIN ingesteld die nodig is om gebruikers en/of producten te verwijderen en om de streeplijst te legen en te mailen. Tot slot kan er worden doorgeklikt naar de "Streeplijst Folder" die wordt aangemaakt op de telefoon of tablet zodra de Streeplijst wordt geleegd. Hier worden alle gebruikers en portfoliotabellen als Csv bestand opgeslagen.

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/Design2.PNG" height="5%" width="80%"/> 
</p>  

## UML-Diagram
In de app kunnen producten en gebruikers worden toegevoegd. Deze worden opgeslagen in de 'products table' en 'users table' in de StreepDatabase. Wanneer er een product wordt gestreept wordt de transactie opgeslagen in de 'transactions table'. Tegelijkertijd worden dan ook de 'portfolio table', de 'users table'en de 'products table' aangepast omdat er een transactie heeft plaats gevonden. In de 'portfolio table' wordt voor elke gebruiker onder andere bijgehouden welke producten hij of hij zij heeft gestreept en hoeveel hij of zij daar van heeft gestreept. In de app wordt gebruik gemaakt van verschillende GridView's en ListViews om de gebruikers, producten, transacties en portfolios weer te geven. Dit wordt gedaan middels de verschillende adapters die de informatie krijgen vanuit de database. 

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/UML.PNG" height="5%" width="80%"/> 
</p>  

## Process
Er is op een aantal punten afgeweken van het oorspronkelijke design. Hieronder een samenvatting van de belangrijkste veranderingen: 

In eerste instantie wat het idee om via een knop in de ActionBar door te klikken naar een 'MenuActivity'. Het doorklikken naar een aparte activiteit is echter onnodig als het ook mogelijk is om het menu in één keer te open in de vorm van een Navigation Drawer. Daarnaast konden gebruikers in de eerste fase slechts één keer worden geselecteerd. Indien een gebruiker twee drankjes zou willen strepen dan zou hij of zij het Streepproces twee keer moeten doorlopen. Dit is wederom onnodig. Daarom is het nu mogelijk om gebruikers meerdere keren te selecteren. In het oorspronkelijke plan waren bovendien niet de portfolio's opgenomen. Al snel bleek echter dat het transactieoverzicht de gebruiker onvoldoende inzich zou geven in zijn of haar uitgaven. Daarom is besloten om apart de portfolio's bij te houden. Ook van het plan om gebruikers en producten te kunnen verwijderen door ze langer ingedrukt te houden is afgeweken. Bij het productenoverzicht wordt de gebruiker nu doorverwezen naar de profielpagina van het product. De ratio hierachter is dat een gebruiker over het algemeen vaker de profielpagina van een product zal willen inzien, dan dat een gebruiker een product zal willen verwijderen. Indien een gebruik langer wordt ingedrukt is er de mogelijkheid om het selecteren te resetten of in één keer een grote hoeveelheid te strepen. Toen de app door een paar huisgenoten werd getest kwam al snel naar voren dat zij het onhandig vonden dat er geen mogelijkheid was om het selecteren ongedaan te maken. De profielpagina's van de gebruikers zijn daarom bereikbaar via een apart gebruikersoverzicht.
