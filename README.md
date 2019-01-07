# Eindproject: Automatische Streeplijst

*Anne Hoogerduijn Strating*  
*12441163*  

**Samenvatting**  
Indien in een (studenten)huis bier, fris of andere producten gezamenlijk worden ingekocht en aan het einde van de maand worden verrekend, kan een app de consumpties van iedere huisgenoot bijhouden en hier aan het einde van de maand een financieel overzicht van geven. 

**Probleem**  
Ik woon in een groot studentenhuis (45 man) waar elke maand al het bier en fris wordt geleverd. Op de ijskasten hangen streeplijsten: één voor bier, één voor fris en één voor speciaal bier. Deze streeplijsten worden aan het eind van de maand met de hand geteld daarna wordt er voor iedere huisgenoot een factuur opgesteld. 

Dit 'streep-systeem' is om verschillende redenen niet optimaal. Het kost onnodige tijd en moeite om de streeplijsten iedere maand handmatig te tellen en elke keer nieuwe streeplijsten op te hangen. Daarnaast zijn de streeplijsten niet altijd duidelijk af te lezen. Bovendien kan er maar in beperkte mate onderscheid worden gemaakt tussen de prijzen van bepaalde producten (bier, speciaal bier en fris). Zo wordt al het speciaal bier onder dezelfde prijs geschaard omdat het onpraktisch is om voor elke soort een aparte streeplijst aan te maken. Hierdoor is ook het aanbod beperkt.


**Idea**  
Een app die deze streeplijsten vervangt en aan het eind van de maand automatisch een overzicht verstuurt naar de persoon die de facturen opstelt.

**Project**  
*Main features:*  
* Het kunnen bijhouden van de consumptie (product, hoeveelheid, totale kosten) van minimaal 45 gebruikers.  
* Aan het einde van de maand automatisch een overzichtsmail sturen naar de huisgenoot die de facturen opstelt.  
* Hierna automatisch de database/streeplijst legen.
* Strepen kunnen verwijderen indien er iets fout is gegaan
* Transactieoverzicht bijhouden

*Optioneel:*  
* De mogelijkheid om in-app:  
      * producten toe te voegen of te verwijderen (bij voorkeur pin vereist zodat niet iedere gek dit kan doen)  
      * gebruikters te verwijderen en nieuwe toe te voegen (bij verhuizen, pin vereist)  
      * in één keer meerdere consumpties strepen  
      * persoonlijke date in te zien (hoeveel deze maand uitgegeven en waaraan etc.)  
* Bijhouden wat er het meest wordt gedronken
* App koppelen aan eboekhouden.nl indien mogelijk (excel kan je importeren).
* Meerdere tablets op zelfde app aansluiten?

**Doelgroep**  
Studenten

**Datasources**  
Geen, alleen gebruikers input?

**External components**  
SQLite om gegevens van de gebruikers bij te houden

**Similar**  
Vergelijkbaar met de finance opdracht gezien het feit dat in een database voor elke gebruiker zal moeten worden bijgehouden welke producten de gebruiker heeft gekocht, hoeveel de gebruiker daaarvan heeft gekocht en wat de totale kosten/uitgaven zijn. Bovendien is één van de belangrijkste optionele features dat de gebruiker zijn of haar data in kan zien.

**Hardest Parts**
- Veel gebruikers input. Belangrijk om ervoor te zorgen dat dit de app niet in de war kan gooien. 
- Ervoor zorgen dat elke maand een duidelijk overzicht wordt gemaild naar de persoon die de facturen opstelt.
- Gebruiksvriendelijkheid. De app moet snel en overzichtelijk zijn, anders gebruikt iedereen liever een streeplijst.
- Moet 100% betrouwbaar zijn omdat er maandelijkses facturen op worden gebaseerd.


**Sketch**  
<p align="center">
     <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/voorbeeld_schets.png" height="50%" width="50%"/>
</p>
