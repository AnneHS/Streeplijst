# Report: Streeplijst

*Anne Hoogerduijn Strating*  
*12441163*  

**Samenvatting**  
Indien een groep gezamenlijk drank ingekoopt kan met deze app worden bijgehouden wie wat heeft gedronken zodat de kosten aan het einde van een door gebruikter te bepalen termijn gemakkelijk kunnen worden verrekend.  

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/ProductsActivity.jpg" height="5%" width="24%"/> 
</p>  

## Design
In de app kunnen producten en gebruikers worden toegevoegd. Deze worden opgeslagen in de 'products table' en 'users table' in de StreepDatabase. Wanneer er een product wordt gestreept wordt de transactie opgeslagen in de 'transactions table'. Tegelijkertijd worden dan ook de 'portfolio table', de 'users table'en de 'products table' aangepast omdat er een transactie heeft plaats gevonden. In de 'portfolio table' wordt voor elke gebruiker onder andere bijgehouden welke producten hij of hij zij heeft gestreept en hoeveel hij of zij daar van heeft gestreept. In de app wordt gebruik gemaakt van verschillende GridView's en ListViews om de gebruikers, producten, transacties en portfolios weer te geven. Dit wordt gedaan middels de verschillende adapters die de informatie krijgen vanuit de database. 

<p align="center">
  <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/UML.PNG" height="5%" width="80%"/> 
</p>  

