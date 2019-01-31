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

In de app kunnen producten en gebruikers worden toegevoegd. Deze worden opgeslagen in de 'products table' en 'users table' in de StreepDatabase. Wanneer er een product wordt gestreept wordt de transactie opgeslagen in de 'transactions table'. Tegelijkertijd worden dan ook de 'portfolio table', de 'users table'en de 'products table' aangepast omdat er een transactie heeft plaats gevonden. In de 'portfolio table' wordt voor elke gebruiker onder andere bijgehouden welke producten hij of hij zij heeft gestreept en hoeveel hij of zij daar van heeft gestreept. In de app wordt gebruik gemaakt van verschillende GridView's en ListViews om de gebruikers, producten, transacties en portfolios weer te geven. Dit wordt gedaan middels de verschillende adapters die de informatie krijgen vanuit de database. 

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/UML.PNG" height="5%" width="80%"/> 
</p>  

